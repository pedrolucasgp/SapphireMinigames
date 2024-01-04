package me.sapphire.bowspleef.structures;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.goldengamerzone.worldreset.WorldReset;
import me.sapphire.bowspleef.main.Main;
import me.sapphire.bowspleef.utils.BukkitConfig;
import me.sapphire.bowspleef.utils.Mine;

public class GameStateManager {
	private static BukkitConfig config = new BukkitConfig("config.yml");

	public static GameStateEnum currentState = GameStateEnum.WAITING;

	public static int defaultWaitingTimer = 20; // 5 minutes;
	public static int waitingTimer = defaultWaitingTimer;
	public static Integer maxPlayers = Integer.valueOf(config.getString("maxplayers"));
	
	public static ItemStack doubleJumpActive = Mine.newItem(Material.FEATHER, "§aDouble Jump");
	public static ItemStack doubleJumpInactive = Mine.newItem(Material.BARRIER, "§cDouble Jump");
	public static int maxDoubleJumpValue = Integer.valueOf(config.getString("doublejumpnumber"));
	public static int maxTripleShotValue = 5;
	public static int maxRepulsorValue = 5;

	public static HashMap<Player, Integer> doubleJumpCount = new HashMap<>();
	public static HashMap<Player, Integer> tripleShotCount = new HashMap<>();
	public static HashMap<Player, Integer> repulsorCount = new HashMap<>();
	
	public static List<Player> spectators = new ArrayList<>();

	public void initializeGame() {
		currentState = GameStateEnum.WAITING;
	}

	public void initializeTimer() {
		currentState = GameStateEnum.TIMER;
	}

	public void stopTimer() {
		currentState = GameStateEnum.WAITING;
	}

	public void startGame() {
		currentState = GameStateEnum.GAME;
	}

	public void setItems(Player player) {
		ItemStack bow = Mine.newItem(Material.BOW, null);
		bow.getItemMeta().spigot().setUnbreakable(true);
		ItemStack arrow = Mine.newItem(Material.ARROW, null, 1);
		
		Mine.addEnchant(bow, Enchantment.ARROW_INFINITE, 1);
		Mine.addEnchant(bow, Enchantment.ARROW_FIRE, 1);

		player.getInventory().setItem(Mine.getPosition(1, 1), bow);
		player.getInventory().setItem(Mine.getPosition(1, 9), doubleJumpActive);
		player.getInventory().setItem(Mine.getPosition(4, 1), arrow);
	}

	private Integer endGameTimer;

	public void endGame(Player winner) {
        currentState = GameStateEnum.FINISHED;
        
        Location spawnLocation = config.getLocation("spawnLocation");

        List<Player> allPlayers = (List<Player>) Bukkit.getOnlinePlayers();
        List<Player> loserPlayers = allPlayers.stream()
                .filter(player -> !player.getName().equalsIgnoreCase(winner.getName())).collect(Collectors.toList());

        Mine.sendTitle(winner, "§b§lYOU WIN", "§a" + winner.getName() + " §ewon the match!", 10, 200, 10);

        for (Player loser : loserPlayers) {
            Mine.sendTitle(loser, "§C§lYOU LOSE", "§a" + winner.getName() + " §ewon the match!", 10, 200, 10);
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {
			player.teleport(spawnLocation);
			player.setAllowFlight(true);
			player.setFlying(true);
		}


        endGameTimer = 8;

        new BukkitRunnable() {
            @Override
            public void run() {
                endGameTimer--;

                if (endGameTimer <= 0) {

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        Bukkit.getMessenger().registerOutgoingPluginChannel(Main.getPlugin(Main.class), "BungeeCord");

                        ByteArrayOutputStream b = new ByteArrayOutputStream();
                        DataOutputStream out = new DataOutputStream(b);
                        try {
                            out.writeUTF("Connect");
                            out.writeUTF("lobby");
                        } catch (IOException ex) {
                            ex.printStackTrace();
                            player.sendMessage("§cImpossible to connect. support@sapphiremc.cc");
                        }
                        player.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", b.toByteArray());
                    }

                    resetGameState();
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 20);

        new BukkitRunnable() {

            @Override
            public void run() {
                Mine.newFirework(winner.getLocation(), 1, Color.RED, Color.BLUE, true, true);

                if (endGameTimer <= 0) {
                    cancel();
                }
            }
        }.runTaskTimer(Main.getPlugin(Main.class), 0, 10);

    }
	
	public static void resetGameState() {
		waitingTimer = defaultWaitingTimer;
	}
	
	public void addPlayerToSpectator(Player player) {
		spectators.add(player);
		player.setGameMode(GameMode.CREATIVE);
		player.addPotionEffect(
		new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
		Mine.clearInventory(player);
	}

	public boolean isPlayerSpectator(Player player) {
		return spectators.contains(player);
	}
	
	public static void saveBackupWorld(String worldName) {
        if(!WorldReset.worldSaved(worldName)) {
            WorldReset.saveWorld(worldName);
            System.out.println("World "+worldName+" has saved.");
        }
    }

    public static void resetWorld(String worldName) {
        WorldReset.resetWorld(worldName);
    }
}