package fr.not_here.not_holo_lib.commands

import fr.not_here.not_holo_lib.classes.NotHolo
import org.bukkit.Material
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Item
import org.bukkit.entity.Player

class EditHolo : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player){
            sender.sendMessage("§cYou must be a player to use this command")
            return true
        }
        var holoEntity = sender.world.rayTraceEntities(sender.eyeLocation, sender.location.direction, 10.0, 1.0)?.hitEntity

        if(holoEntity is Item){
            holoEntity = if(holoEntity.isInsideVehicle) holoEntity.vehicle else holoEntity
        }

        if(holoEntity !is ArmorStand){
            sender.sendMessage("§cYou must look at a holo")
            return true
        }

        val notHolo = NotHolo.fromArmorStand(holoEntity)

        if(notHolo == null){
            sender.sendMessage("§cThis armor stand is not a holo")
            return true
        }

        if(args.isEmpty()){
            sender.sendMessage("§cYou must specify a line to edit")
            return true
        }

        if (args[0].toIntOrNull() == null){
            sender.sendMessage("§cThe line must be a number")
            return true
        }

        val line = args[0].toInt()

        if(args[1].isEmpty()){
            sender.sendMessage("§cYou must specify a new text or item")
            return true
        }

        if(Material.values().map { it.name }.contains(args[1]))
            notHolo.editItem(line, null, Material.valueOf(args[1]))
        else
            notHolo.editLine(line, null, args[1])

        return true
    }
}