package xyz.zeeraa.skywarsleaderboard;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import net.md_5.bungee.api.ChatColor;

public class SkyWarsLeaderboard extends JavaPlugin {
	private File scoreFile;
	private Hologram hologram;

	private String scoreboardTitle;
	private String scoreboardFormat;
	private int scoreboardLines;

	@Override
	public void onEnable() {
		saveDefaultConfig();

		String path = (getConfig().getString("score_file_path").startsWith(".") ? getDataFolder().getAbsolutePath() + "/" : "") + getConfig().getString("score_file_path");

		getLogger().info("Cheking if " + path + " exists...");

		scoreFile = new File(path);

		if (!scoreFile.exists()) {
			getLogger().severe(path + " does not exists. Please check if score_file_path is correct in config.yml");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		ConfigurationSection locationCfg = getConfig().getConfigurationSection("hologram_location");

		String wordName = locationCfg.getString("world_name");

		getLogger().info("Configured world: " + wordName);

		Location hologramLocation = new Location(Bukkit.getServer().getWorld(wordName), locationCfg.getDouble("x"), locationCfg.getDouble("y"), locationCfg.getDouble("z"));
		hologram = HologramsAPI.createHologram(this, hologramLocation);

		hologram.setAllowPlaceholders(true);

		scoreboardTitle = getConfig().getString("title");
		scoreboardFormat = getConfig().getString("format");
		scoreboardLines = getConfig().getInt("lines");

		int updateInterval = getConfig().getInt("update_interval");

		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				updateHologram();
			}
		}, updateInterval, updateInterval);

		updateHologram();
	}

	public void updateHologram() {
		try {
			ArrayList<ScoreData> score = ScoreReader.readFile(scoreFile);

			score = ScoreSorter.getTopScore(score, scoreboardLines);

			if (hologram.size() == 0) {
				hologram.appendTextLine(scoreboardTitle);
			}

			for (int i = 0; i < score.size(); i++) {
				ScoreData scoreData = score.get(i);

				String lineText = "" + scoreboardFormat;

				lineText = lineText.replace("%place%", "" + (i + 1));
				lineText = lineText.replace("%username%", scoreData.getUsername());
				lineText = lineText.replace("%score%", "" + scoreData.getScore());
				lineText = lineText.replace("%rank%", "" + scoreData.getRank());
				
				lineText = ChatColor.translateAlternateColorCodes('&', lineText);

				if (hologram.size() - 1 <= i) {
					hologram.appendTextLine(lineText);
				} else {
					((TextLine) hologram.getLine(i + 1)).setText(lineText);
				}
			}
		} catch (Exception e) {
			getLogger().warning("An error occurred while processing score data. Stack trace");
			e.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		HandlerList.unregisterAll(this);

		if (hologram != null) {
			hologram.delete();
		}
	}
}