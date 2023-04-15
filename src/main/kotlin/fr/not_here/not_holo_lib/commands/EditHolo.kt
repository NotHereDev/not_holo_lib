package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.extension.lookingNotHolo
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class EditHolo(
    override val autoCompletion: Array<NotHoloCommand.(CommandSender) -> MutableList<String>?> = arrayOf(
        { sender ->
            this.getCachedHoloFromSender(sender)?.run {
                Array(lineCount.coerceAtLeast(itemCount)){ it.toString() }.toMutableList()
            }
        },
        { sender ->
            this.getCachedHoloFromSender(sender)?.run {
                (listOf("removeItem","removeLine") + lines.map { it } + items.map { it.name }).toMutableList()
            }
        },
    )
) : NotHoloCommand() {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = requirePlayer(sender) ?: return false
        val notHolo = player.lookingNotHolo ?: return player.respondError("You must look at a holo")
        val line = args.getOrNull(0)?.toIntOrNull() ?: return player.respondError("The line must be specified and a number")

        when(val text = args.getOrNull(1) ?: ""){
            "removeItem" -> notHolo.removeItem(line)
            "removeLine" -> notHolo.removeLine(line)
            else -> {
                if(Material.values().map { it.name }.contains(text))
                    notHolo.editItem(line, Material.valueOf(text))
                else
                    notHolo.editLine(line, text)
            }
        }

        return true
    }
}