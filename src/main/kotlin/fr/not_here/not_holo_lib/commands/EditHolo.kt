package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.extension.lookingNotHolo
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class EditHolo(
    override val autoCompletion: Array<NotHoloCommand.(CommandSender) -> MutableList<String>?> = arrayOf(
        { sender ->
            val holo = this.getCachedHoloFromSender(sender)
            if(holo == null){
                null
            } else {
                Array(holo.lineCount.coerceAtLeast(holo.itemCount)){ it.toString() }.toMutableList()
            }
        },
        { sender ->
            val holo = this.getCachedHoloFromSender(sender)
            if(holo == null){
                null
            } else {
                (listOf("removeItem","removeLine") + holo.lines.map { it } + holo.items.map { it.name }).toMutableList()
            }
        },
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

        if (args.getOrNull(0)?.toIntOrNull() == null){
            sender.sendMessage("§cThe line must be specified and a number")
            return true
        }

        val line = args[0].toInt()
        val text = args.getOrNull(1) ?: ""

        when(text){
            "removeItem" -> {
                notHolo.removeItem(line)
                return true
            }
            "removeLine" -> {
                notHolo.removeLine(line)
                return true
            }
        }

        if(Material.values().map { it.name }.contains(text))
            notHolo.editItem(line, Material.valueOf(text))
        else
            notHolo.editLine(line, text)

        return true
    }
}