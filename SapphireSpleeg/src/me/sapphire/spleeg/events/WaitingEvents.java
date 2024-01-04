package me.sapphire.spleeg.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.sapphire.spleeg.main.Main;
import me.sapphire.spleeg.structures.GameStateEnum;
import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.structures.SimplesScore;
import me.sapphire.spleeg.utils.BukkitConfig;
import me.sapphire.spleeg.utils.Mine;

public class WaitingEvents implements Listener {

	BukkitTask timer = null;
	GameStateManager gameStateManager = new GameStateManager();
	Integer waitingTime = GameStateManager.waitingTimer;
	Integer maxPlayers = GameStateManager.maxPlayers;

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		BukkitConfig config = new BukkitConfig("config.yml");
		Location spawnLocation = config.getLocation("spawnLocation");

		Collection<? extends Player> players = p.getServer().getOnlinePlayers();
		List<? extends Player> playerList = new ArrayList<>(players);

		gameStateManager.initializeGame();
		if (p != null && spawnLocation != null) {
            p.teleport(spawnLocation);
        }

		//minimum players
		if (playerList.size() >= maxPlayers / 2) {
			gameStateManager.initializeTimer();



			Bukkit.broadcastMessage("§eThe minimum number of players has entered. Starting in " + Mine.formatarTempoMS(waitingTime));
			p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);

			timer = new BukkitRunnable() {

				@Override
				public void run() {
					waitingTime-- ;
					GameStateManager.waitingTimer = waitingTime;

					if(playerList.size() == maxPlayers ) {
						waitingTime = 10;
						Bukkit.broadcastMessage("§eAll players have entered. Starting in " + Mine.formatarTempoMS(waitingTime));
					}
					if (waitingTime == 10) {
						for (Player player : playerList) {
							player.teleport(spawnLocation);
						}
					}
					if (waitingTime <= 10) {
						Mine.sendTitle("§a§l" + waitingTime, "§eStarting in", 10, 20, 10);
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
					}

					if (waitingTime <= 0) {
						p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0f, 1.0f);
						gameStateManager.startGame();
						
						for (Player player : playerList) {
							SimplesScore.setScore(player);
							gameStateManager.setItems(player);
						}

						cancel();
					}
				}
			}.runTaskTimer(Main.getPlugin(Main.class), 0, 20);
		}
	}

	@EventHandler
	public void onLeft(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();

		if (GameStateManager.currentState == GameStateEnum.TIMER) {
			gameStateManager.initializeGame();

			if (timer != null) {
				timer.cancel();
				Bukkit.broadcastMessage(
						"§cThere are not enough players to start the match. The timer has been frozen.");

				for (Player player : players) {
					player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1.0f, 1.0f);
				}
			}
		}
	}
}
