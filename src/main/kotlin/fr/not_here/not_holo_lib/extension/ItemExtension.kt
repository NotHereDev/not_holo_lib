@file:JvmName("ItemExtension")
package fr.not_here.not_holo_lib.extension

import fr.not_here.not_holo_lib.NotHoloLib
import org.bukkit.NamespacedKey
import org.bukkit.entity.Item
import org.bukkit.persistence.PersistentDataType

var Item.isPickable: Boolean
    get() {
        return this.persistentDataContainer.get(NamespacedKey(NotHoloLib.instance, "is_pickable"), PersistentDataType.SHORT) != 0.toShort()
    }
    set(value) {
        this.persistentDataContainer.set(NamespacedKey(NotHoloLib.instance, "is_pickable"), PersistentDataType.SHORT, if (value) 1.toShort() else 0.toShort())
    }

var Item.canDespawn: Boolean
    get() {
        return this.persistentDataContainer.get(NamespacedKey(NotHoloLib.instance, "can_despawn"), PersistentDataType.SHORT) != 0.toShort()
    }
    set(value) {
        this.persistentDataContainer.set(NamespacedKey(NotHoloLib.instance, "can_despawn"), PersistentDataType.SHORT, if (value) 1.toShort() else 0.toShort())
    }

val Item.toHolo : Item
    get() {
        return this.apply {
            isPickable = false
            pickupDelay = Int.MAX_VALUE
            isPersistent = true
            canDespawn = false
        }
    }