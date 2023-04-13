package fr.not_here.not_holo_lib.listeners

import fr.not_here.not_holo_lib.extension.canDespawn
import fr.not_here.not_holo_lib.extension.isPickable
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.entity.ItemDespawnEvent

class ItemListener : Listener {
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onItemPickup(event: EntityPickupItemEvent){
        if(!event.item.isPickable){
            event.isCancelled = true
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    fun onItemDespawn(event: ItemDespawnEvent){
        if(!event.entity.canDespawn){
            event.isCancelled = true
        }
    }
}