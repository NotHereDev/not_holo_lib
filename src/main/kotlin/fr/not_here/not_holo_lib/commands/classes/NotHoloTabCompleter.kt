package fr.not_here.not_holo_lib.commands.classes

import fr.not_here.not_holo_lib.NotHoloLib
import fr.not_here.not_holo_lib.commands.NotHoloCommand
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class NotHoloTabCompleter : TabCompleter {
    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): MutableList<String>? {
        val executor = NotHoloLib.instance.getCommand(command.name)?.executor
        if (executor is NotHoloCommand) {
            val autoCompletion = executor.autoCompletion

            return if (autoCompletion.size >= args.size) {
                autoCompletion[args.size - 1](executor, sender)?.filter { it.startsWith(args[args.size - 1]) } ?.toMutableList()
            } else {
                autoCompletion.lastOrNull()?.run { this(executor, sender)?.filter { it.startsWith(args.lastOrNull() ?: "") } ?.toMutableList() }
            }
        }
        return null
    }
}