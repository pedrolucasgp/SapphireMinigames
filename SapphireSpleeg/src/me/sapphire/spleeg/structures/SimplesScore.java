package me.sapphire.spleeg.structures;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.sapphire.spleeg.structures.GameStateEnum;
import me.sapphire.spleeg.structures.GameStateManager;
import me.sapphire.spleeg.events.GeneralEvents;
import me.sapphire.spleeg.events.ItemsEvents;
import me.sapphire.spleeg.main.Main;
import me.sapphire.spleeg.utils.DisplayBoard;
import me.sapphire.spleeg.utils.Mine;

public class SimplesScore extends BukkitRunnable implements Listener {

	private static HashMap<Player, DisplayBoard> scoreboards = new HashMap<>();
	
	@EventHandler
	public void aoEntrar(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		BukkitTask timer = null;

		if (timer == null) {
			timer = new BukkitRunnable() {

				@Override
				public void run() {
					for (Player player : players) {
						atualizar(player);
					}
				}
			}.runTaskTimer(Main.getPlugin(Main.class), 0, 10);
		}
	}

	@EventHandler
	public void aoTeleport(PlayerTeleportEvent e) {
		Player p = e.getPlayer();
		setScore(p);
		atualizar(p);
	}

	public static void ligar(JavaPlugin plugin) {
		SimplesScore simpleScore = new SimplesScore();
		simpleScore.runTaskTimerAsynchronously(plugin, 20, 20);
		Bukkit.getPluginManager().registerEvents(simpleScore, plugin);
		;
	}

	public static void setScore(Player p) {
		DisplayBoard scoreboard = new DisplayBoard("�6�lMEU SERVER", "�aLINHA1", "�aLinha2", "", "�aLinha4");
		scoreboard.apply(p);
		scoreboard.clear();
		scoreboard.getLines().clear();
		scoreboards.put(p, scoreboard);
	}

	@EventHandler
    public void aoSair(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        scoreboards.remove(p);

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        for (Player player : players) {
            if (player != null) {
                atualizar(player);
            }
        }

    }

	public void run() {
		for (Player p : Bukkit.getOnlinePlayers()) {
			atualizar(p);
		}
	}

	public static void atualizar(Player p) {
		Collection<? extends Player> players = Bukkit.getOnlinePlayers();

		String fullServer = p.getServer().toString();

		int totalPlayers = players.size();

		Pattern pattern = Pattern.compile("-(\\w+)");

		Matcher matcher = pattern.matcher(fullServer);

		ArrayList<String> stringsArray = new ArrayList<>();
		
		String doubleJumpCount = GameStateManager.doubleJumpCount.get(p) == null ? String.valueOf(GameStateManager.maxDoubleJumpValue) : String.valueOf(GameStateManager.doubleJumpCount.get(p));


		while (matcher.find()) {
			stringsArray.add(matcher.group(1));
		}
		
		Collection<? extends Player> playersCollection = p.getServer().getOnlinePlayers();
		List<Player> playersList = new ArrayList<>(playersCollection);
		Integer playersAlive = 0;

        for (Player player : p.getServer().getOnlinePlayers()) {
            if (!player.isDead()) {
                playersAlive++;
            }
        }

		String[] stringsResult = stringsArray.toArray(new String[0]);

		String serverToken = stringsResult[2];

		DisplayBoard scoreboard = scoreboards.get(p);
		if (scoreboard == null)
			return;

		if(GameStateManager.currentState == GameStateEnum.WAITING) {
			scoreboard.setDisplay("§b§lSPLEEG  ");
			scoreboard.set(9, "§8" + serverToken.toString());
			scoreboard.empty(8);
			scoreboard.set(7, " §fWe need more players ");
			scoreboard.set(6, " §fto initialize the timer.");
			scoreboard.set(5, "");
			scoreboard.set(4, " §fPlayers: §a"+playersList.size()+"/"+GameStateManager.maxPlayers);
			scoreboard.empty(3);
			scoreboard.set(2, "§7" + Main.config.getString("website"));
			scoreboard.empty(1);
			return;
		}
		
		if (GameStateManager.currentState == GameStateEnum.TIMER) {
			scoreboard.setDisplay("§b§lSPLEEG  ");
			scoreboard.set(9, "§8" + serverToken.toString());
			scoreboard.empty(8);
			scoreboard.set(7, " §fStarting in: §a"+Mine.formatarTempoMS(GameStateManager.waitingTimer)+"");
			scoreboard.empty(6);
			scoreboard.set(5, "");
			scoreboard.set(4, " §fPlayers: §a"+playersList.size()+"/"+GameStateManager.maxPlayers);
			scoreboard.empty(3);
			scoreboard.set(2, "§7" + Main.config.getString("website"));
			scoreboard.empty(1);
			return;
		}
		
		
		if(GameStateManager.currentState == GameStateEnum.GAME) {
			scoreboard.setDisplay("§b§lSPLEEG  ");
			scoreboard.set(9, "§8" + serverToken.toString());
			scoreboard.empty(8);
			scoreboard.set(7, " §fDouble Jumps: §a"+doubleJumpCount+"/"+GameStateManager.maxDoubleJumpValue);
			scoreboard.empty(6);
			scoreboard.set(5, "");
			scoreboard.set(4, " §fPlayers remaning: §a"+playersAlive);
			scoreboard.empty(3);
			scoreboard.set(2, "§7" + Main.config.getString("website"));
			scoreboard.empty(1);
			return;
		}
	}

}
