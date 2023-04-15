package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.extension.lookingNotHolo
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemoveHolo(
    override val autoCompletion: Array<NotHoloCommand.(CommandSender) -> MutableList<String>?> = arrayOf(
        { _ -> null }
    )
) : NotHoloCommand() {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = requirePlayer(sender) ?: return false
        val notHolo = player.lookingNotHolo?.remove() ?: return player.respondError("You must look at a holo")
        return true
    }
}