package fr.bluesty.tridimentionalplugin;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.entity.Player;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.bukkit.block.Block;
import org.bukkit.FluidCollisionMode;
import org.bukkit.util.RayTraceResult;

import java.util.HashMap;
import java.util.UUID;

public class GearListener implements Listener {
    private final JavaPlugin plugin;
    private final HashMap<UUID, Long> lastClickTime = new HashMap<>();

    public GearListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Vérifie si le joueur tient les jambières spéciales
        if (player.getInventory().getLeggings() != null && player.getInventory().getLeggings().getType() == Material.DIAMOND_LEGGINGS) {
            if (lastClickTime.containsKey(playerId)) {
                long timeSinceLastClick = System.currentTimeMillis() - lastClickTime.get(playerId);
                if (timeSinceLastClick < 500) { // 500 ms pour un double-clic
                    // Exécuter la logique de déplacement
                    launchPlayerToLocation(player);
                    lastClickTime.remove(playerId);
                } else {
                    // Mise à jour du temps pour le prochain clic
                    lastClickTime.put(playerId, System.currentTimeMillis());
                }
            } else {
                lastClickTime.put(playerId, System.currentTimeMillis());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        lastClickTime.remove(playerId);
                    }
                }.runTaskLater(plugin, 10L); // Supprime l'entrée après un court délai, si un double clic n'est pas détecté
            }
        }
    }

    private void launchPlayerToLocation(Player player) {
        // Obtenir la position des yeux du joueur et la direction dans laquelle il regarde
        Location eyeLocation = player.getEyeLocation();
        Vector direction = eyeLocation.getDirection();

        // Effectuer un raytrace pour trouver le bloc ciblé

        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), 50, FluidCollisionMode.NEVER, true);

        // Si rayTraceResult est null ou si getHitBlock retourne null, ne rien faire
        if (rayTraceResult == null || rayTraceResult.getHitBlock() == null) {
            return; // Quitte simplement la méthode sans effectuer d'action supplémentaire
        }

        // À ce stade, rayTraceResult et getHitBlock() ne sont pas nulls
        Block hitBlock = rayTraceResult.getHitBlock();


        if (hitBlock != null) {
            // Calculer la direction vers le bloc ciblé
            Location hitLocation = hitBlock.getLocation().add(0.5, 0, 0.5); // Centre du bloc
            Vector toBlock = hitLocation.toVector().subtract(player.getLocation().toVector()).normalize();

            // Appliquer la force ou le mouvement vers ce bloc
            player.setVelocity(toBlock.multiply(new Vector(2, 2, 2))); // Ajustez selon la vitesse désirée
        }
    }
}
