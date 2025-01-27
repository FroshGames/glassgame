package am.FroshGames.glassgame.listener;

import am.FroshGames.glassgame.Glassgame;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class GlassListener implements Listener {

    private final Glassgame plugin;
    private final FileConfiguration config;

    public GlassListener(Glassgame plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // Verifica que el jugador cambie de bloque
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }

        Player player = event.getPlayer();
        Block blockUnder = player.getLocation().getBlock();

        // Comprobación de área restringida (si restricted-area es true)
        if (config.getBoolean("restricted-area")) {
            String rWorld = config.getString("restricted-world", "world");
            int minX = config.getInt("minX", 0);
            int maxX = config.getInt("maxX", 0);
            int minZ = config.getInt("minZ", 0);
            int maxZ = config.getInt("maxZ", 0);

            // Si no está en el mundo o en el rango, no hacemos nada
            if (!blockUnder.getWorld().getName().equals(rWorld) ||
                    blockUnder.getX() < minX || blockUnder.getX() > maxX ||
                    blockUnder.getZ() < minZ || blockUnder.getZ() > maxZ) {
                return;
            }
        }

        // Bloque falso desde el config
        Material falseMaterial = Material.matchMaterial(config.getString("block-to-break", "TINTED_GLASS"));
        if (falseMaterial == null) {
            // Si la config es inválida, salimos
            return;
        }

        // Si el bloque es el material "falso"
        if (blockUnder.getType() == falseMaterial) {

            // Mensaje al pisar
            String stepMessage = config.getString("message-on-step", "§c¡Has pisado cristal falso! Toda la plataforma se romperá...");
            player.sendMessage(stepMessage);

            // Retraso en ticks
            int breakDelay = config.getInt("break-delay", 10);

            // Tarea programada para romper la plataforma
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Rompemos la plataforma conectada
                    breakPlatform(blockUnder, falseMaterial);

                    // Mensaje tras romper
                    String breakMessage = config.getString("message-on-break", "§7La plataforma se ha roto...");
                    player.sendMessage(breakMessage);

                    // Si kill-player = true, matamos al jugador
                    if (config.getBoolean("kill-player", false)) {
                        player.setHealth(0.0);
                        String deathMessage = config.getString("message-on-death", "§4¡Has muerto al pisar el bloque falso!");
                        player.sendMessage(deathMessage);
                    }
                }
            }.runTaskLater(plugin, breakDelay);
        }
    }

    /**
     * Rompe todos los bloques del material "falseMaterial" conectados al bloque inicial.
     * Aplica BFS en 2D o 3D según la config.
     */
    private void breakPlatform(Block startBlock, Material falseMaterial) {
        // Lectura de si se busca en 2D o 3D
        boolean searchIn2D = config.getBoolean("search-in-2d", true);

        Queue<Block> queue = new LinkedList<>();
        Set<Block> visited = new HashSet<>();

        queue.add(startBlock);
        visited.add(startBlock);

        // Direcciones 2D (N, S, E, O)
        int[][] DIRECTIONS_2D = {
                {1, 0},  // +X
                {-1, 0}, // -X
                {0, 1},  // +Z
                {0, -1}  // -Z
        };

        // Si queremos 3D, incluimos desplazamientos en Y
        // 6 direcciones: arriba, abajo, norte, sur, este, oeste
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
            // Rompemos el bloque
            if (current.getType() == falseMaterial) {
                current.setType(Material.AIR);
            }

            if (searchIn2D) {
                // Búsqueda en la misma capa
                int cx = current.getX();
                int cy = current.getY();
                int cz = current.getZ();

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
                // Búsqueda en 3D
                int cx = current.getX();
                int cy = current.getY();
                int cz = current.getZ();

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