package fr.not_here.not_holo_lib

import fr.not_here.not_holo_lib.commands.EditHolo
import fr.not_here.not_holo_lib.commands.RemoveHolo
import fr.not_here.not_holo_lib.commands.SpawnHolo
import fr.not_here.not_holo_lib.listeners.ItemPickupListener
import org.bukkit.plugin.java.JavaPlugin
import java.util.logging.Logger

class NotHoloLib : JavaPlugin() {

    override fun onEnable() {
        instance = this
        this.getCommand("spawnholo")?.setExecutor(SpawnHolo())
        this.getCommand("editholo")?.setExecutor(EditHolo())
        this.getCommand("removeholo")?.setExecutor(RemoveHolo())

        this.server.pluginManager.registerEvents(ItemPickupListener(), this)

        logger.info("NotHoloLib enabled")
    }

    override fun onDisable() {
    }

    companion object {
        lateinit var instance: NotHoloLib
            private set
    }
}