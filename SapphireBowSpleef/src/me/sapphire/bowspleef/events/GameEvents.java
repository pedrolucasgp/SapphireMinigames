package me.sapphire.bowspleef.events;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.checkerframework.checker.units.qual.Force;

import me.sapphire.bowspleef.main.Main;
import me.sapphire.bowspleef.structures.GameStateEnum;
import me.sapphire.bowspleef.structures.GameStateManager;
import me.sapphire.bowspleef.structures.SimplesScore;
import me.sapphire.bowspleef.utils.BukkitConfig;
import me.sapphire.bowspleef.utils.Mine;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

public class GameEvents implements Listener {

	GameStateManager gameStateManager = new GameStateManager();
	BukkitConfig config = new BukkitConfig("config.yml");
	Location spawnLocation = config.getLocation("spawnLocation");
	public static List<Player> spectators = new ArrayList<>();

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		Integer playerCount = Bukkit.getServer().getOnlinePlayers().size() - 1;

		e.setQuitMessage(
				"§c" + p.getName() + "§e has left. §7(" + playerCount + "/" + GameStateManager.maxPlayers + ")");
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onPlayerSneak(PlayerToggleSneakEvent e) {

		Player p = e.getPlayer();

		if (e.isSneaking()) {
			repelNearbyPlayers(p);
		}

	}

	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent e) {
		Player p = e.getPlayer();
		e.setCancelled(true);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		e.setDeathMessage(null);

		e.getDrops().clear();

		p.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(Main.class), () -> {
			e.getEntity().spigot().respawn();

			if (GameStateManager.currentState == GameStateEnum.GAME) {
				Bukkit.broadcastMessage("§c" + p.getName() + " §ehas eliminated.");
				Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

				List<? extends Player> winner = players.stream().filter(player -> !player.getName().equals(p.getName()))
						.collect(Collectors.toList());

				gameStateManager.addPlayerToSpectator(p);

				gameStateManager.endGame(winner.get(0));
			}

		}, 1L);
	}

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		Location spawnLocation = config.getLocation("spawnLocation");

		e.setRespawnLocation(spawnLocation);
	}

	@EventHandler
	public void onPlayerItemHeld(PlayerItemHeldEvent e) {
		Player p = e.getPlayer();
		int newSlot = e.getNewSlot();

		if (p.getInventory().getItem(newSlot) != null && p.getInventory().getItem(newSlot).getType().getId() == 261) {
			sendActionBar(p, ChatColor.YELLOW + "§lClick with left button to triple shoot");
		} else {
			sendActionBar(p, "");
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		double maxDistance = 5.0;
		if (GameStateManager.currentState == GameStateEnum.GAME) {
			for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
				if (otherPlayer != p && otherPlayer.getLocation().distance(p.getLocation()) <= maxDistance) {
					double distance = p.getLocation().distance(otherPlayer.getLocation());
					if (distance < maxDistance) {
						Mine.sendActionBar(p, "§e§lClick in shift to use the repulsor.");
					}
				}
			}
		}
	}

	public void sendActionBar(Player p, String message) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

		try {
			Class<?> packetPlayOutChatClass = Class.forName("net.minecraft.server." + version + ".PacketPlayOutChat");
			Class<?> iChatBaseComponentClass = Class.forName("net.minecraft.server." + version + ".IChatBaseComponent");
			Class<?> chatSerializerClass = Class
					.forName("net.minecraft.server." + version + ".IChatBaseComponent$ChatSerializer");
			Object chatBaseComponent = chatSerializerClass.getMethod("a", String.class).invoke(null,
					"{\"text\":\"" + message + "\"}");
			Constructor<?> packetPlayOutChatConstructor = packetPlayOutChatClass.getConstructor(iChatBaseComponentClass,
					byte.class);
			Object packet = packetPlayOutChatConstructor.newInstance(chatBaseComponent, (byte) 2);

			Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
			Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);
			playerConnection.getClass()
					.getMethod("sendPacket", Class.forName("net.minecraft.server." + version + ".Packet"))
					.invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void repelNearbyPlayers(Player p) {

		double repelRadius = 5.0;
		double upwardForce = 0.2;

		Integer repulsorCount = GameStateManager.repulsorCount.get(p);
		if (GameStateManager.currentState == GameStateEnum.GAME) {
			if (repulsorCount == null) {
				GameStateManager.repulsorCount.put(p, GameStateManager.maxRepulsorValue);
				repulsorCount = GameStateManager.repulsorCount.get(p);
			}
			if (GameStateManager.repulsorCount.get(p) > 0) {
				p.playSound(p.getLocation(), Sound.GHAST_FIREBALL, 1.0f, 1.0f);
				int repulsorCountCopy = repulsorCount -= 1;
				GameStateManager.repulsorCount.put(p, repulsorCountCopy);
				SimplesScore.atualizar(p);

				for (Player nearbyPlayer : Bukkit.getServer().getOnlinePlayers()) {
					if (nearbyPlayer != p && nearbyPlayer.getLocation().distance(p.getLocation()) <= repelRadius) {
						Vector pushDirection = nearbyPlayer.getLocation().toVector()
								.subtract(p.getLocation().toVector()).normalize();
						pushDirection.setY(upwardForce);
						nearbyPlayer.setVelocity(pushDirection.multiply(2));
					}
				}
			}
		}

	}

	public void addPlayerToSpectator(Player player) {
		spectators.add(player);
		player.setGameMode(GameMode.CREATIVE);
		player.addPotionEffect(
				new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
	}

	public boolean isPlayerSpectator(Player player) {
		return spectators.contains(player);
	}
}
