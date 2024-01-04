package me.sapphire.bowspleef.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

import me.sapphire.bowspleef.commands.SetConfigCommand;
import me.sapphire.bowspleef.commands.SetLocationCommand;
import me.sapphire.bowspleef.events.GameEvents;
import me.sapphire.bowspleef.events.GeneralEvents;
import me.sapphire.bowspleef.events.ItemsEvents;
import me.sapphire.bowspleef.events.WaitingEvents;
import me.sapphire.bowspleef.structures.GameStateManager;
import me.sapphire.bowspleef.structures.SimplesScore;
import me.sapphire.bowspleef.utils.BukkitConfig;

public class Main extends JavaPlugin {

    public static BukkitConfig config;

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SimplesScore(), this);
        Bukkit.getPluginManager().registerEvents(new GeneralEvents(), this);
        Bukkit.getPluginManager().registerEvents(new WaitingEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ItemsEvents(), this);
        Bukkit.getPluginManager().registerEvents(new GameEvents(), this);

        getCommand("setlocation").setExecutor(new SetLocationCommand());
        getCommand("setconfig").setExecutor(new SetConfigCommand());
        
        String workDirectory = System.getProperty("user.dir");

        String folderNameGameMaps = "GameMaps";

        File directoryGameMaps = new File(workDirectory, folderNameGameMaps);

        if (directoryGameMaps.exists() && directoryGameMaps.isDirectory()) {
            File[] subdirectories = directoryGameMaps.listFiles(File::isDirectory);

            if (subdirectories != null) {
                for (File subdirectory : subdirectories) {
                    GameStateManager.saveBackupWorld(subdirectory.getName());
                    Bukkit.createWorld(new WorldCreator(subdirectory.getName()));
                }
            }
        }

        config = new BukkitConfig("config.yml", this);
        config.saveDefaultConfig();

        Bukkit.getConsoleSender().sendMessage("§b[SapphireBowSpleef] §6Bow Spleef has started.");
    }
    
    public void clearAllDrops() {
    	for (World world : Bukkit.getWorlds()) {
    		for(Entity entity : world.getEntities()) {
    			if (entity instanceof Item) {
    				entity.remove();
    			}
    		}
    	}
    }
    
}
