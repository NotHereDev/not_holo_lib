package fr.not_here.not_holo_lib.classes

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.extension.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.util.Vector




private const val ARMOR_STAND_HEIGHT = 0.0;
class NotHolo(
    var loc: Location,
    var spacing: Double = 0.3,
    private val mainArmorStand: ArmorStand = (loc.chunk.world.spawnEntity(loc, EntityType.ARMOR_STAND) as ArmorStand).apply { toUnnamedHolo(); },
    private val lineArmorStands: MutableMap<Vector, ArmorStand> = mutableMapOf(),
    private val itemsArmorStands: MutableMap<Vector, ArmorStand> = mutableMapOf(),
) {

    fun addLine(offset: Vector?, line: String){
        loc = mainArmorStand.location
        loc.add(offset ?: Vector(0.0, spacing * lineArmorStands.size, 0.0))
        val e = loc.chunk.world.spawnEntity(
            //we want to remobe armor stand height from the location
            loc.apply { subtract(0.0, ARMOR_STAND_HEIGHT, 0.0) },
            EntityType.ARMOR_STAND
        ) as ArmorStand;
        e.toHolo()
        e.customName = line
        e.mainArmorStand = mainArmorStand
        mainArmorStand.relatedArmorStands += e
        lineArmorStands[offset ?: Vector(0.0, spacing * lineArmorStands.size, 0.0)] = e
    }

    fun editLine(index: Int, offset: Vector?, line: String){
        if(lineArmorStands.size <= index) return
        loc = mainArmorStand.location
        val e = lineArmorStands.entries.elementAt(index)
        loc.add(offset ?: e.key)
        NotHoloLib.instance.logger.info("parent: ${e.value.mainArmorStand}")
        e.value.teleport(loc.apply { subtract(0.0, ARMOR_STAND_HEIGHT, 0.0) })
        e.value.customName = line
        NotHoloLib.instance.logger.info("parent: ${e.value.mainArmorStand}")
        if(offset != null) {
            lineArmorStands.remove(e.key)
            lineArmorStands[offset] = e.value
        }
    }

    fun removeLine(index: Int){
        if(itemsArmorStands.size <= index) return
        val e = lineArmorStands.entries.elementAt(index)
        e.value.remove()
        mainArmorStand.relatedArmorStands -= e.value
        lineArmorStands.remove(e.key)
    }

    fun addItem(offset: Vector?, material: Material){
        loc = mainArmorStand.location
        loc.add(offset ?: Vector(0.0, spacing * itemsArmorStands.size, 0.0))
        val e = loc.chunk.world.spawnEntity(
            //we want to remobe armor stand height from the location
            loc.apply { subtract(0.0, ARMOR_STAND_HEIGHT, 0.0) },
            EntityType.ARMOR_STAND
        ) as ArmorStand;
        e.toUnnamedHolo()
        val item = loc.chunk.world.dropItem(loc, org.bukkit.inventory.ItemStack(material))
        item.isPersistent = true
        item.isPickable = false
        item.teleport(loc)
        e.addPassenger(item)
        e.mainArmorStand = mainArmorStand
        mainArmorStand.relatedArmorStands += e
        itemsArmorStands[offset ?: Vector(0.0, spacing * itemsArmorStands.size, 0.0)] = e
    }

    fun editItem(index: Int, offset: Vector?, material: Material){
        if(itemsArmorStands.size <= index) return
        loc = mainArmorStand.location
        val e = itemsArmorStands.entries.elementAt(index)
        loc.add(offset ?: e.key)
        e.value.teleport(loc.apply { subtract(0.0, ARMOR_STAND_HEIGHT, 0.0) })
        val item = e.value.passengers[0] as Item
        item.itemStack = org.bukkit.inventory.ItemStack(material)
        if (offset != null) {
            itemsArmorStands.remove(e.key)
            itemsArmorStands[offset] = e.value
        }
    }

    fun removeItem(index: Int){
        if(itemsArmorStands.size <= index) return
        val e = itemsArmorStands.entries.elementAt(index)
        e.value.remove()
        mainArmorStand.relatedArmorStands -= e.value
        itemsArmorStands.remove(e.key)
    }

    fun remove(){
        mainArmorStand.remove()
        lineArmorStands.values.forEach { it.remove() }
        itemsArmorStands.values.forEach { it.passengers.forEach{ it2 -> it2.remove() }; it.remove() }
    }

    companion object{
        fun fromArmorStand(armorStand: ArmorStand): NotHolo? {
            var armorStands = listOf<ArmorStand>()
            armorStand.mainArmorStand?.also {
                armorStands = it.relatedArmorStands
            }
            if(armorStands.isEmpty()) armorStands = armorStand.relatedArmorStands
            if(armorStands.isEmpty()) return null
            val mainArmorStand = armorStands.first().mainArmorStand ?: return null
            val lineArmorStands = mutableMapOf<Vector, ArmorStand>().apply { putAll(armorStands.filter { it.passengers.isEmpty() }.map { it.location.toVector().subtract(mainArmorStand.location.toVector()) to it }) }
            val itemsArmorStands = mutableMapOf<Vector, ArmorStand>().apply { putAll(armorStands.filter { it.passengers.isNotEmpty() }.map { it.location.toVector().subtract(mainArmorStand.location.toVector()) to it }) }
            return NotHolo(mainArmorStand.location, mainArmorStand = mainArmorStand, lineArmorStands = lineArmorStands, itemsArmorStands = itemsArmorStands)
        }
    }
}