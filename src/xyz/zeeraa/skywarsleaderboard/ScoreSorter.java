package xyz.zeeraa.skywarsleaderboard;

import java.util.ArrayList;
import java.util.Collections;

public class ScoreSorter {
	public static ArrayList<ScoreData> getTopScore(ArrayList<ScoreData> score, int maxEntries) {
		Collections.sort(score);

		while (score.size() > maxEntries) {
			score.remove(score.size() - 1);
		}

		return score;
	}
}
