@file:JvmName("LocationExtension")
package fr.not_here.not_holo_lib.extension

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.Item
import org.bukkit.inventory.ItemStack


//we want to remove armor stand height from the location
private const val ARMOR_STAND_TAG_OFFSET = 0.0
val Location.alignForTag: Location
    get() = this.subtract(0.0, ARMOR_STAND_TAG_OFFSET, 0.0)

private const val ARMOR_STAND_PASSENGER_OFFSET = 0.0
val Location.alignForPassenger: Location
    get() = this.subtract(0.0, ARMOR_STAND_PASSENGER_OFFSET, 0.0)

val Location.spawnArmorStand: ArmorStand
    get() = this.chunk.world.spawnEntity(
        this.alignForTag,
        EntityType.ARMOR_STAND
    ) as ArmorStand

fun Location.spawnItem(material: Material): Item? {
    if(material == Material.AIR) return null
    return this.chunk.world.dropItem(this.alignForTag, ItemStack(material))
}

object SLocation{
    fun from(x: Double, y: Double, z: Double, world: String? = null): Location {
        return Location(
            if (world == null) Bukkit.getWorld("world") else Bukkit.getWorld(world), x, y, z
        )
    }

    val zero: Location
        get() = from(0.0, 0.0, 0.0)

    fun zero(world: String? = null): Location {
        return from(0.0, 0.0, 0.0, world)
    }
}

