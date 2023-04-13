@file:JvmName("PlayerExtension")
package fr.not_here.not_holo_lib.extension

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.classes.NotHolo
import net.md_5.bungee.api.chat.hover.content.Item
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player

val Player.lookingNotHolo: NotHolo?
    get() = this.world.rayTraceEntities(
        this.eyeLocation,
        this.location.direction,
        32.0,
        1.0,
    ){ it is ArmorStand }?.hitEntity.run {
        if(this is ArmorStand) NotHolo.fromArmorStand(this) else null
    }