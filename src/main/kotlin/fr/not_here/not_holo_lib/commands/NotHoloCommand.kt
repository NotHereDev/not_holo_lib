package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.classes.NotHolo
import fr.not_here.not_holo_lib.extension.lookingNotHolo
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.util.*
import java.util.concurrent.Executors

abstract class NotHoloCommand : CommandExecutor {
    abstract val autoCompletion: Array<NotHoloCommand.(executor: CommandSender) -> MutableList<String>?>

    private var cachedHoloForSender: MutableMap<UUID, NotHolo?> = mutableMapOf()

    fun getCachedHoloFromSender(sender: CommandSender): NotHolo? {
        if (sender !is Player) return null
        if (cachedHoloForSender.contains(sender.uniqueId)) return cachedHoloForSender[sender.uniqueId]
        cachedHoloForSender[sender.uniqueId] = sender.lookingNotHolo
        Executors.newSingleThreadScheduledExecutor().schedule({
            cachedHoloForSender.remove(sender.uniqueId)
        }, 1, java.util.concurrent.TimeUnit.SECONDS)
        return cachedHoloForSender[sender.uniqueId]
    }
}