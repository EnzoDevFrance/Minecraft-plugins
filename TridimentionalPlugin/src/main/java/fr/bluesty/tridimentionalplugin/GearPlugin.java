package fr.bluesty.tridimentionalplugin;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import org.bukkit.plugin.java.JavaPlugin;

public class GearPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new GearListener(this), this);
        getLogger().info("Le sang de tous ses os qu'il a été activé ce pluggin de mort!");
    }

    @Override
    public void onDisable() {
        getLogger().info("GearPlugin has been disabled.");
    }
}

