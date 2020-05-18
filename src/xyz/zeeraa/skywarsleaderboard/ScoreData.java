package xyz.zeeraa.skywarsleaderboard;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class ScoreData implements Comparable<ScoreData> {
	private UUID uuid;
	private int rank;
	private int score;
	private String username;

	public ScoreData(UUID uuid, int rank, int score, String username) {
		this.uuid = uuid;
		this.rank = rank;
		this.score = score;
		this.username = username;
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getScore() {
		return score;
	}
	
	public String getUsername() {
		return username;
	}
	
	public OfflinePlayer getPlayer() {
		return Bukkit.getServer().getOfflinePlayer(uuid);
	}

	@Override
	public int compareTo(ScoreData o) {
		return o.getScore() - this.getScore();
	}
}