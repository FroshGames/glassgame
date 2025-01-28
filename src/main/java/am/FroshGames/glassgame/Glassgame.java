package am.FroshGames.glassgame; // Debe coincidir con plugin.yml

import am.FroshGames.glassgame.commands.GlassCommand;
import am.FroshGames.glassgame.listener.GlassListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Glassgame extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();

        // Registro del comando
        if (getCommand("glassgame") != null) {
            getCommand("glassgame").setExecutor(new GlassCommand(this));
        }
        getServer().getPluginManager().registerEvents(new GlassListener(this), this);

        getLogger().info("Glassgame se ha habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Glassgame se ha deshabilitado.");
    }
}

// Pluguin echo por Froshy //Amir chiquito estubo aqui