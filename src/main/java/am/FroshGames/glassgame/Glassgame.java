package am.FroshGames.glassgame;

import am.FroshGames.glassgame.listener.GlassListener;
import org.bukkit.plugin.java.JavaPlugin;;

public class Glassgame extends JavaPlugin {


    @Override
    public void onEnable() {
        // Carga la config.yml (si no existe, la genera)
        saveDefaultConfig();

        getLogger().info("GlassBridgePlugin se ha habilitado.");

        // Registrar el listener
        getServer().getPluginManager().registerEvents(new GlassListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("GlassBridgePlugin se ha deshabilitado.");
    }
}