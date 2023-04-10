package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.classes.NotHolo
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SpawnHolo : CommandExecutor{
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val location = if(sender is Player){
            sender.location
        }else{
            Location(Bukkit.getWorld("world"), 0.0, 0.0, 0.0)
        }

        val holo = NotHolo(location)

        for(arg in args){
            if(Material.values().map { it.name }.contains(arg)){
                holo.addItem(null, Material.valueOf(arg))
            }else{
                holo.addLine(null, arg)
            }
        }

        return true
    }


}