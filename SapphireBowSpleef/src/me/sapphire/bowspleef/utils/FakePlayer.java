package me.sapphire.bowspleef.utils;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Jogador Off Ficticio<br>
 *  nome;id
 * @author Eduard-PC
 *
 */
public class FakePlayer implements OfflinePlayer {

	private String name;
	private UUID id;
//
	public void setName(String name) {
		this.name = name;
	}

	public FakePlayer(String name) {
		this.name = name;
		setIdByName();

	}

	public void setIdByName() {
		try {
			this.id = UUID.nameUUIDFromBytes(("OfflinePlayer:" + name).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public FakePlayer(String name, UUID id) {
		this(name);
		this.setId(id);
	}

	public FakePlayer(UUID id) {
		this(null, id);
	}

	public FakePlayer(OfflinePlayer player) {
		this(player.getName(), player.getUniqueId());
	}

	public FakePlayer() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FakePlayer other = (FakePlayer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public boolean isOp() {
		return false;
	}

	@Override
	public void setOp(boolean op) {

	}

	@Override
	public Map<String, Object> serialize() {
		HashMap<String, Object> mapaNovo = new HashMap<>();
		mapaNovo.put("name", this.name);
		mapaNovo.put("uuid", this.getUniqueId());
		return mapaNovo;
	}

	@Override
	public Location getBedSpawnLocation() {
		return null;
	}

	@Override
	public long getFirstPlayed() {
		return 0;
	}

	@Override
	public long getLastPlayed() {
		return 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Player getPlayer() {
		Player player = null;
		if (id != null)
			player = Bukkit.getPlayer(id);
		if (player != null)
			return player;
		if (name != null) {
			player = Bukkit.getPlayer(name);
		}
		return player;
	}

	@Override
	public UUID getUniqueId() {
		return id;
	}

	@Override
	public boolean hasPlayedBefore() {
		return true;
	}

	@Override
	public boolean isBanned() {
		return false;
	}

	@Override
	public boolean isOnline() {
		return getPlayer() != null;
	}

	@Override
	public boolean isWhitelisted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setBanned(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setWhitelisted(boolean arg0) {
		// TODO Auto-generated method stub

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}
