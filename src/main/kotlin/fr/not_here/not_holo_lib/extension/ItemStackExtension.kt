@file:JvmName("ItemStackExtension")

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
