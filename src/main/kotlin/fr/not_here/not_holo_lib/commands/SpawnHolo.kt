package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.classes.NotHolo
import fr.not_here.not_holo_lib.extension.SLocation
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnHolo(
    override val autoCompletion: Array<NotHoloCommand.(CommandSender) -> MutableList<String>?> = arrayOf(
        { _ -> mutableListOf("textOrMaterialName") }
    )
) : NotHoloCommand() {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val location = if(sender is Player) sender.location else SLocation.zero
        val holo = NotHolo(location)

        for(arg in args){
            if(Material.values().map { it.name }.contains(arg)){
                holo.addItem(Material.valueOf(arg))
            }else{
                holo.addLine(arg)
            }
        }

        return true
    }


}