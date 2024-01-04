package me.sapphire.spleeg.events;

import me.sapphire.spleeg.main.Main;
import me.sapphire.spleeg.structures.GameStateEnum;
import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.structures.SimplesScore;
import me.sapphire.spleeg.utils.BukkitConfig;
import net.minecraft.server.v1_8_R3.PlayerAbilities;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Egg;
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
	public void launchPadEvent(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		Block blockBelow = p.getLocation().subtract(0, 1, 0).getBlock();

		if (p.getLocation().getBlock().getRelative(BlockFace.DOWN).getType() == Material.GOLD_BLOCK) {
			p.playSound(p.getLocation(), Sound.FIREWORK_LAUNCH, 100.0f, 100.0f);
			new BukkitRunnable() {

				@Override
				public void run() {
					Vector direction = p.getEyeLocation().getDirection().normalize().multiply(GameStateManager.launchBoost);
					Vector upVector = new Vector(0, GameStateManager.launchBoost, 0);
					p.setVelocity(direction.add(upVector));
				}
			}.runTaskLaterAsynchronously(Main.getPlugin(Main.class), 1);

		}
	}

	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent e) {
	
		e.blockList().clear();
		e.setCancelled(true);

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
	
	public void addPlayerToSpectator(Player player) {
        spectators.add(player);
        player.setGameMode(GameMode.CREATIVE);
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, Integer.MAX_VALUE, false, false));
    }

    public boolean isPlayerSpectator(Player player) {
        return spectators.contains(player);
    }
}

