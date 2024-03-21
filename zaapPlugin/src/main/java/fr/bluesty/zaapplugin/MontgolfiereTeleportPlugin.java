package fr.bluesty.zaapplugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class MontgolfiereTeleportPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        // Coordonnées de la montgolfière

        int targetX = -185, targetY = -8, targetZ = 13;
        int errorMargin = 2;

        if (Math.abs(loc.getBlockX() - targetX) <= errorMargin &&
                Math.abs(loc.getBlockY() - targetY) <= errorMargin &&
                Math.abs(loc.getBlockZ() - targetZ) <= errorMargin) {
            Inventory inv = Bukkit.createInventory(null, 9, "Choisissez votre destination");

            // Destination 1
            ItemStack dest1 = new ItemStack(Material.ENDER_PEARL);
            ItemMeta meta1 = dest1.getItemMeta();
            meta1.setDisplayName("Destination 1");
            dest1.setItemMeta(meta1);
            inv.setItem(3, dest1); // Position dans l'inventaire

            // Destination 2
            ItemStack dest2 = new ItemStack(Material.NETHER_STAR);
            ItemMeta meta2 = dest2.getItemMeta();
            meta2.setDisplayName("Destination 2");
            dest2.setItemMeta(meta2);
            inv.setItem(5, dest2); // Position dans l'inventaire

            player.openInventory(inv);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Choisissez votre destination")) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || !clickedItem.hasItemMeta()) return;

        String itemName = clickedItem.getItemMeta().getDisplayName();

        // Coordonnées des destinations
        if (itemName.equals("Destination 1")) {
            player.teleport(new Location(player.getWorld(), -219, -15, 159));
        } else if (itemName.equals("Destination 2")) {
            player.teleport(new Location(player.getWorld(), -195, -14, -66));
        }
    }
}