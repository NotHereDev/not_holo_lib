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
        if(sender !is Player){
            sender.sendMessage("§cYou must be a player to use this command")
            return true
        }

        val notHolo = sender.lookingNotHolo

        if(notHolo == null){
            sender.sendMessage("§cYou must look at a holo")
            return true
        }

        notHolo.remove()

        return true
    }
}