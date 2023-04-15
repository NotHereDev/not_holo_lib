package fr.not_here.not_holo_lib

import fr.not_here.not_holo_lib.commands.EditHolo
import fr.not_here.not_holo_lib.commands.RemoveHolo
import fr.not_here.not_holo_lib.commands.SpawnHolo
import fr.not_here.not_holo_lib.commands.classes.NotHoloTabCompleter
import fr.not_here.not_holo_lib.listeners.ItemListener
import org.bukkit.plugin.java.JavaPlugin

class NotHoloLib : JavaPlugin() {

    override fun onEnable() {
        instance = this
        @Suppress("SpellCheckingInspection")
        mapOf(
            "spawnholo" to SpawnHolo(),
            "editholo" to EditHolo(),
            "removeholo" to RemoveHolo()
        ).forEach { (name, executor) ->
            this.getCommand(name)?.setExecutor(executor)
            this.getCommand(name)?.tabCompleter = NotHoloTabCompleter()
        }

        this.server.pluginManager.registerEvents(ItemListener(), this)

        logger.info("NotHoloLib enabled")
    }

    override fun onDisable() {
    }

    companion object {
        lateinit var instance: NotHoloLib
            private set
    }
}