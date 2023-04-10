@file:JvmName("ArmorStandExtension")
package fr.not_here.not_holo_lib.extension

import fr.not_here.not_holo_lib.NotHoloLib
import org.bukkit.NamespacedKey
import org.bukkit.entity.ArmorStand
import org.bukkit.persistence.PersistentDataType
import java.util.*


fun ArmorStand.toHolo() {
    this.isCustomNameVisible = true
    this.isInvulnerable = true
    this.isSilent = true
    this.setGravity(false)
    this.isMarker = true
    this.isVisible = false
    this.isCollidable = false
}
fun ArmorStand.toUnnamedHolo() {
    this.isCustomNameVisible = false
    this.isInvulnerable = true
    this.isSilent = true
    this.setGravity(false)
    this.isMarker = true
    this.isVisible = false
    this.isCollidable = false
}

var ArmorStand.relatedArmorStands: List<ArmorStand>
    get() {
        val list = mutableListOf<ArmorStand>()
        val relatedArmorStandUUIDSStr = this.persistentDataContainer.get(NamespacedKey(NotHoloLib.instance, "related_armor_stands"), PersistentDataType.STRING)
            ?: return mutableListOf()
        val relatedArmorStandUUIDS = relatedArmorStandUUIDSStr.split("|");
        for (it in relatedArmorStandUUIDS) {
            val uuid: UUID
            try {
                uuid = UUID.fromString(it)
            } catch (e: IllegalArgumentException) {
                continue
            }
            val e = this.world.entities.find { it2 -> it2.uniqueId == uuid } as ArmorStand? ?: continue
            list.add(e)
        }
        return list
    }
    set(value) {
        val uuids = value.joinToString("|") { it.uniqueId.toString() }
        this.persistentDataContainer.set(NamespacedKey(NotHoloLib.instance, "related_armor_stands"), PersistentDataType.STRING, uuids)
    }

var ArmorStand.mainArmorStand: ArmorStand?
    get() {
        val mainArmorStandUUIDStr = this.persistentDataContainer.get(NamespacedKey(NotHoloLib.instance, "main_armor_stand"), PersistentDataType.STRING)
            ?: return null
        val mainArmorStandUUID: UUID
        try {
            mainArmorStandUUID = UUID.fromString(mainArmorStandUUIDStr)
        } catch (e: IllegalArgumentException) {
            return null
        }
        return this.world.entities.find { it2 -> it2.uniqueId == mainArmorStandUUID } as ArmorStand?
    }
    set(value) {
        this.persistentDataContainer.set(NamespacedKey(NotHoloLib.instance, "main_armor_stand"), PersistentDataType.STRING, value?.uniqueId.toString())
    }

