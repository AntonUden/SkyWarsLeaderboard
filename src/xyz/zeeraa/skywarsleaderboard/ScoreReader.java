package xyz.zeeraa.skywarsleaderboard;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

public class ScoreReader {
	public static ArrayList<ScoreData> readFile(File file) throws IOException {
		ArrayList<ScoreData> result = new ArrayList<ScoreData>();
		
		String data = FileUtils.readFileToString(file, "UTF-8");

		JSONObject jsonData = new JSONObject(data);
		
		JSONObject players = jsonData.getJSONObject("uuid-players-v1");
		
		for(String uuid : players.keySet()) {
			JSONObject playerData = players.getJSONObject(uuid);
			
			result.add(new ScoreData(UUID.fromString(uuid), playerData.getInt("rank"), playerData.getInt("score"), playerData.getString("username")));
		}
		
		return result;
	}
}