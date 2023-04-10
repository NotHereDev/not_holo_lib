package fr.not_here.not_holo_lib.listeners

import fr.not_here.not_holo_lib.extension.isPickable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent

class ItemPickupListener : Listener {
    @EventHandler(priority = org.bukkit.event.EventPriority.HIGHEST, ignoreCancelled = true)
    fun onItemPickup(event: EntityPickupItemEvent){
        if(!event.item.isPickable){
            event.isCancelled = true
        }
    }
}