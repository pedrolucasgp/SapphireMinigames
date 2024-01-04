package me.sapphire.spleeg.structures;

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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import de.goldengamerzone.worldreset.WorldReset;
import me.sapphire.spleeg.main.Main;
import me.sapphire.spleeg.utils.BukkitConfig;
import me.sapphire.spleeg.utils.Mine;

public class GameStateManager {
	private static BukkitConfig config = new BukkitConfig("config.yml");

	public static GameStateEnum currentState = GameStateEnum.WAITING;

	public static int defaultWaintingTimer = 15; // 5 minutes;
	public static int waitingTimer = defaultWaintingTimer;
	public static Integer maxPlayers = Integer.valueOf(config.getString("maxplayers"));
	public static Integer launchBoost = Integer.valueOf(config.getString("launchnumber"));

	public static ItemStack doubleJumpActive = Mine.newItem(Material.FEATHER, "§a§lDouble Jump");
	public static ItemStack doubleJumpInactive = Mine.newItem(Material.BARRIER, "§c§lDouble Jump");
	public static int maxDoubleJumpValue = Integer.valueOf(config.getString("doublejumpnumber"));

	public static HashMap<Player, Integer> doubleJumpCount = new HashMap<>();

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
		ItemStack shovel = Mine.newItem(Material.IRON_SPADE, "§b§lSPLEEG");
		player.getInventory().setItem(Mine.getPosition(1, 1), shovel);
		player.getInventory().setItem(Mine.getPosition(1, 9), doubleJumpActive);
		player.updateInventory();
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
		waitingTimer = defaultWaintingTimer;
		spectators = new ArrayList<>();
		doubleJumpCount = new HashMap<>();
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


