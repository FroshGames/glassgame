package am.FroshGames.glassgame;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class Glassgame extends JavaPlugin implements Listener {

    private Set<Location> safePlatforms = new HashSet<>();
    private Set<Location> fakePlatforms = new HashSet<>();

    @Override
    public void onEnable() {
        // Register the event listener
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Glassgame plugin enabled!");

        // Load configuration
        loadPlatformConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("Glassgame plugin disabled!");
    }

    private void loadPlatformConfig() {
        File configFile = new File(getDataFolder(), "platforms.yml");
        if (!configFile.exists()) {
            saveResource("platforms.yml", false);
        }

        FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

        config.getMapList("platforms").forEach(platform -> {
            String type = (String) platform.get("type");
            int x = (int) platform.get("coordinates.x");
            int y = (int) platform.get("coordinates.y");
            int z = (int) platform.get("coordinates.z");
            Location location = new Location(Bukkit.getWorld("world"), x, y, z);

            if ("safe".equalsIgnoreCase(type)) {
                safePlatforms.add(location);
            } else if ("fake".equalsIgnoreCase(type)) {
                fakePlatforms.add(location);
            }
        });
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        Block block = playerLocation.getBlock();

        if (block.getType() == Material.TINTED_GLASS && player.getGameMode() == GameMode.ADVENTURE) {
            if (fakePlatforms.contains(playerLocation)) {
                // Remove the 3x3 area of tinted glass blocks
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            Block adjacentBlock = block.getRelative(x, y, z);
                            if (adjacentBlock.getType() == Material.TINTED_GLASS) {
                                adjacentBlock.setType(Material.AIR);
                            }
                        }
                    }
                }
            }
        }
    }
}