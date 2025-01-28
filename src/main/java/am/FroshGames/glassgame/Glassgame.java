package am.FroshGames.glassgame;

import am.FroshGames.glassgame.listener.GlassListener;
import org.bukkit.plugin.java.JavaPlugin;;

public class Glassgame extends JavaPlugin {

    @Override
    public void onEnable() {
        // Carga o crea el config.yml automáticamente (si no existe)
        saveDefaultConfig();

        // Registra el listener que contiene la lógica del juego
        getServer().getPluginManager().registerEvents(new GlassListener(this), this);

        getLogger().info("Glassgame se ha habilitado correctamente.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Glassgame se ha deshabilitado.");
    }
}

// Pluguin echo por Froshy //Amir chiquito estubo aqui