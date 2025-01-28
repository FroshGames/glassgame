package am.FroshGames.glassgame;

import am.FroshGames.glassgame.commands.GlassCommand;
import am.FroshGames.glassgame.listener.GlassListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Glassgame extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        getServer().getPluginManager().registerEvents(new GlassListener(this), this);

        // Registro del comando
        if (getCommand("glassgame") != null) {
            getCommand("glassgame").setExecutor(new GlassCommand(this));
        }

        getLogger().info("Glassgame se ha habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Glassgame se ha deshabilitado.");
    }
}


// Pluguin echo por Froshy //Amir chiquito estubo aqui