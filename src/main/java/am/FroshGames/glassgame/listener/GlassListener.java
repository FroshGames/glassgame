package am.FroshGames.glassgame.listener;

import am.FroshGames.glassgame.Glassgame;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class GlassListener implements Listener {

    private final Glassgame plugin;
    private final FileConfiguration config;

    public GlassListener(Glassgame plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Evita que los jugadores en modo espectador activen el evento
        if (player.getGameMode() == GameMode.SPECTATOR) {
            return;
        }

        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        Block blockUnder = player.getLocation().getBlock().getRelative(0, -1, 0); // Ensure we check the block directly under the player

        if (config.getBoolean("restricted-area")) {
            String rWorld = config.getString("restricted-world", "world");
            int minX = config.getInt("minX", 0);
            int maxX = config.getInt("maxX", 0);
            int minZ = config.getInt("minZ", 0);
            int maxZ = config.getInt("maxZ", 0);

            if (!blockUnder.getWorld().getName().equals(rWorld) ||
                    blockUnder.getX() < minX || blockUnder.getX() > maxX ||
                    blockUnder.getZ() < minZ || blockUnder.getZ() > maxZ) {
                return;
            }
        }

        Material falseMaterial = Material.matchMaterial(config.getString("block-to-break", "TINTED_GLASS"));
        if (falseMaterial == null) {
            return;
        }

        if (blockUnder.getType() == falseMaterial) {
            String stepMessage = config.getString("message-on-step",
                    "§c¡Has pisado cristal falso! Toda la plataforma se romperá...");
            player.sendMessage(stepMessage);

            int breakDelay = config.getInt("break-delay", 10);

            new BukkitRunnable() {
                @Override
                public void run() {
                    breakPlatform(blockUnder, falseMaterial);

                    String breakMessage = config.getString("message-on-break",
                            "§7La plataforma se ha roto...");
                    player.sendMessage(breakMessage);

                    if (config.getBoolean("kill-player", false)) {
                        player.setHealth(0.0);

                        String deathMessage = config.getString("message-on-death",
                                "§4¡Has muerto al pisar el bloque falso!");
                        player.sendMessage(deathMessage);
                    }
                }
            }.runTaskLater(plugin, breakDelay);
        }
    }


    private void breakPlatform(Block startBlock, Material falseMaterial) {
        boolean searchIn2D = config.getBoolean("search-in-2d", true);

        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();

        queue.add(startBlock);
        visited.add(startBlock);

        int[][] DIRECTIONS_2D = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        int[][] DIRECTIONS_3D = {
                {1, 0, 0},
                {-1, 0, 0},
                {0, 1, 0},
                {0, -1, 0},
                {0, 0, 1},
                {0, 0, -1}
        };

        while (!queue.isEmpty()) {
            Block current = queue.poll();

            if (current.getType() == falseMaterial) {
                current.setType(Material.AIR);
            }

            int cx = current.getX();
            int cy = current.getY();
            int cz = current.getZ();

            if (searchIn2D) {
                for (int[] dir : DIRECTIONS_2D) {
                    int nx = cx + dir[0];
                    int nz = cz + dir[1];
                    Block neighbor = current.getWorld().getBlockAt(nx, cy, nz);

                    if (!visited.contains(neighbor) && neighbor.getType() == falseMaterial) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            } else {
                for (int[] dir : DIRECTIONS_3D) {
                    int nx = cx + dir[0];
                    int ny = cy + dir[1];
                    int nz = cz + dir[2];
                    Block neighbor = current.getWorld().getBlockAt(nx, ny, nz);

                    if (!visited.contains(neighbor) && neighbor.getType() == falseMaterial) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                }
            }
        }
    }
}
// Desarrollado por Froshy para MialuStudios
