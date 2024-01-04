package me.sapphire.spleeg.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import me.sapphire.spleeg.structures.GameStateEnum;
import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.utils.BukkitConfig;
import me.sapphire.spleeg.utils.Mine;

public class GeneralEvents implements Listener {

	private BukkitConfig config = new BukkitConfig("config.yml");

	GameStateManager gameStateManager = new GameStateManager();

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		Integer playerCount = Bukkit.getServer().getOnlinePlayers().size();
		Integer maxPlayers = Integer.valueOf(config.getString("maxplayers"));
		
		World world = p.getWorld();

		world.setStorm(false);
		world.setWeatherDuration(0);

		e.setJoinMessage("§a" + p.getName() + " §ehas joined. §7(" + playerCount + "/" + maxPlayers+")");

		p.getServer().dispatchCommand(p.getServer().getConsoleSender(), "time set day");
		p.getServer().dispatchCommand(p.getServer().getConsoleSender(), "weather clear " + 100000);

		p.setGameMode(GameMode.SURVIVAL);
		p.setFoodLevel(Integer.MAX_VALUE);
		p.setHealth(20.0);

		Mine.removeEffects(p);
		Mine.clearInventory(p);
		
		p.getServer().dispatchCommand(p.getServer().getConsoleSender(), "gamerule doDaylightCycle false");
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent e) {
		e.setCancelled(true);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		Integer playerCount = Bukkit.getServer().getOnlinePlayers().size();

		e.setQuitMessage("§a" + p.getName() + " §ehas left. §7(" + (playerCount - 1) + "/" + "2)");

		if (GameStateManager.currentState == GameStateEnum.GAME && playerCount >= 2) {
			Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

			List<? extends Player> winner = players.stream().filter(player -> !player.getName().equals(p.getName()))
					.collect(Collectors.toList());

			gameStateManager.endGame(winner.get(0));
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		 if (e.getEntity() instanceof Player) {
	            if (e.getCause() != EntityDamageEvent.DamageCause.VOID) {
	                e.setCancelled(true);
	            }
	        }
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();

		if (GameStateManager.currentState == GameStateEnum.TIMER
				|| GameStateManager.currentState == GameStateEnum.WAITING) {
			e.setCancelled(true);
			p.sendMessage("§cIt is not allowed to type in the chat during the wait.");
		}
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		e.setCancelled(true);
	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory() != null && event.getInventory().getHolder() instanceof Player) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                event.setCancelled(true);
            }
        }
    }
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent e) {
		e.setCancelled(true);
		
	}

}

