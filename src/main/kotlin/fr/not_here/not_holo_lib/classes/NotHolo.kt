package fr.not_here.not_holo_lib.classes

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.extension.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack
import org.bukkit.util.Vector

class NotHolo(
    var loc: Location,
    var spacing: Double = 0.3,
    private val _mainArmorStand: ArmorStand = loc.spawnArmorStand.toHolo,
    private val lineArmorStands: MutableMap<Vector, ArmorStand> = mutableMapOf(),
    private val itemArmorStands: MutableMap<Vector, ArmorStand> = mutableMapOf(),
) {
    private val defaultLineOffset: Vector
        get() = Vector(0.0, spacing * lineArmorStands.size, 0.0)
    private val defaultItemOffset: Vector
        get() = Vector(0.0, spacing * itemArmorStands.size, 0.0)

    val lines: List<String>
        get() = lineArmorStands.values.mapNotNull { it.customName }

    val items: List<Material>
        get() = itemArmorStands.values.mapNotNull { (it.passengers.getOrNull(0) as Item?)?.itemStack?.type }

    val lineCount: Int
        get() = lineArmorStands.size

    val itemCount: Int
        get() = itemArmorStands.size

    fun addLine(line: String, offset: Vector = defaultLineOffset): NotHolo {
        loc = _mainArmorStand.location.add(offset)
        val e = loc.spawnArmorStand.toLineHolo.apply {
            customName = line
            isCustomNameVisible = line.isNotEmpty()
            mainArmorStand = _mainArmorStand
        }
        _mainArmorStand.relatedArmorStands += e
        lineArmorStands[offset] = e
        return this
    }

    fun editLine(index: Int, line: String, offset: Vector? = null): NotHolo {
        if (lineArmorStands.size <= index){
            for (i in lineArmorStands.size until index) addLine("")
            return addLine(line, offset ?: defaultLineOffset)
        }
        val e = lineArmorStands.entries.elementAt(index)
        e.value.apply {
            customName = line
            isCustomNameVisible = line.isNotEmpty()
            teleport(_mainArmorStand.location.add(offset ?: e.key).alignForTag)
        }
        if (offset != null) {
            lineArmorStands.remove(e.key)
            lineArmorStands[offset] = e.value
        }
        return this
    }

    fun removeLine(index: Int): NotHolo {
        if (lineArmorStands.size <= index) return this
        if(lineArmorStands.size == index + 1) {
            lineArmorStands.remove(lineArmorStands.keys.elementAt(index))?.apply { remove(); }
            if(lineArmorStands.isEmpty() && itemArmorStands.isEmpty()) _mainArmorStand.remove()
        } else {
            editLine(index, "")
        }
        removeLastEmptyLines()
        return this
    }

    private fun removeLastEmptyLines(): NotHolo {
        for (i in lineArmorStands.size - 1 downTo 0) {
            if (!lineArmorStands.values.elementAt(i).customName.isNullOrEmpty()) break
            lineArmorStands.remove(lineArmorStands.keys.elementAt(i))?.apply { remove(); }
        }
        return this
    }

    fun addItem(material: Material, offset: Vector = defaultItemOffset): NotHolo {
        NotHoloLib.instance.logger.info("length: ${itemArmorStands.size}, offset: $offset, material: $material, map: $itemArmorStands, related: ${_mainArmorStand.relatedArmorStands} ")
        loc = _mainArmorStand.location.add(offset)
        val i = loc.spawnItem(material)?.toHolo
        val e = loc.spawnArmorStand.toItemHolo.apply{
            if(i != null) addPassenger(i)
            mainArmorStand = _mainArmorStand
        }
        _mainArmorStand.relatedArmorStands += e
        itemArmorStands[offset] = e
        return this
    }

    fun editItem(index: Int, material: Material, offset: Vector? = null): NotHolo {
        NotHoloLib.instance.logger.info("length: ${itemArmorStands.size}, index: $index")
        if (itemArmorStands.size <= index) {
            for (i in itemArmorStands.size until index) addItem(Material.AIR)
            return addItem(material, offset ?: defaultItemOffset)
        }
        val e = itemArmorStands.entries.elementAt(index)
        e.value.apply{
            teleport(_mainArmorStand.location.add(offset ?: e.key).alignForTag)
            (passengers.getOrNull(0) as Item?).let {
                if (it != null && it.itemStack.type != material && material != Material.AIR) it.itemStack = ItemStack(material)
                else if (it == null && material != Material.AIR) addPassenger(loc.spawnItem(material)?.toHolo!!)
                else if (it != null && material == Material.AIR) it.remove()
            }
        }
        if (offset != null) {
            itemArmorStands.remove(e.key)
            itemArmorStands[offset] = e.value
        }
        return this
    }

    fun removeItem(index: Int): NotHolo {
        if (itemArmorStands.size <= index) return this
        if(itemArmorStands.size == index + 1) {
            itemArmorStands.remove(itemArmorStands.keys.elementAt(index))?.apply { passengers.forEach { it2 -> it2.remove() }; remove(); }
            if(lineArmorStands.isEmpty() && itemArmorStands.isEmpty()) _mainArmorStand.remove()
        } else {
            editItem(index, Material.AIR)
        }
        removeLastEmptyItems()
        return this
    }
    private fun removeLastEmptyItems(): NotHolo {
        for (i in itemArmorStands.size - 1 downTo 0) {
            val passenger = itemArmorStands.values.elementAt(i).passengers.getOrNull(0) as Item?
            if (passenger != null && passenger.itemStack.type != Material.AIR) break
            itemArmorStands.remove(itemArmorStands.keys.elementAt(i))?.apply { passengers.forEach { it2 -> it2.remove() }; remove(); }
        }
        return this
    }

    fun remove() {
        _mainArmorStand.remove()
        lineArmorStands.values.forEach { it.remove() }
        itemArmorStands.values.forEach { it.apply { passengers.forEach { it2 -> it2.remove() } }.remove() }
    }

    companion object {
        fun fromArmorStand(armorStand: ArmorStand): NotHolo? {
            val armorStands = armorStand.run { mainArmorStand?.relatedArmorStands ?: relatedArmorStands }
            if (armorStands.isEmpty()) return null
            val mainArmorStand = armorStands.first().mainArmorStand ?: return null
            return NotHolo(
                mainArmorStand.location,
                _mainArmorStand = mainArmorStand,
                lineArmorStands = armorStands.lineHolos.toCacheMap(mainArmorStand.location),
                itemArmorStands = armorStands.itemHolos.toCacheMap(mainArmorStand.location)
            )
        }
    }
}