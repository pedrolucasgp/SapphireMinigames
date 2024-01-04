package me.sapphire.spleeg.main;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.plugin.java.JavaPlugin;

import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.commands.SetConfigCommand;
import me.sapphire.spleeg.commands.SetLocationCommand;
import me.sapphire.spleeg.events.GameEvents;
import me.sapphire.spleeg.events.GeneralEvents;
import me.sapphire.spleeg.events.ItemsEvents;
import me.sapphire.spleeg.events.WaitingEvents;
import me.sapphire.spleeg.structures.SimplesScore;
import me.sapphire.spleeg.utils.BukkitConfig;

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

        config = new BukkitConfig("config.yml", this);
        config.saveDefaultConfig();
        
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

        Bukkit.getConsoleSender().sendMessage("§b[SapphireSpleeg] §6Spleeg has started.");
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
