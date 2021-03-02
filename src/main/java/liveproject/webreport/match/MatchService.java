package liveproject.webreport.match;

import liveproject.webreport.season.Season;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

@Log
@Service
public class MatchService {
	private MatchRepository matchRepository;

	@Autowired
	public MatchService(MatchRepository matchRepository) {
		this.matchRepository = matchRepository;
	}

	@Transactional
	public Match save(Match match) {
		matchRepository.save(match);
		return match;
	}

	@Transactional
	public Map<String, Integer> saveAll(String seasonStr, List<Match> matches) {
		Map<String, Integer> resultsBySeason = new HashMap<>();
		for (Match m : matches) {
			String season = StringUtils.isEmpty(m.getSeason()) ? seasonStr : m.getSeason();
			m.setSeason(season);
			matchRepository.save(m);
			if (!resultsBySeason.containsKey(season)) {
				resultsBySeason.put(season, 0);
			}
			resultsBySeason.put(season, 1+resultsBySeason.get(season));
		}
		return resultsBySeason;
	}

	public Set<Match> getAllBySeasonSorted(String seasonStr) {
		List<Match> matches = matchRepository.findBySeason(seasonStr);
		SortedSet<Match> sortedMatches = new TreeSet<>();
		sortedMatches.addAll(matches);
		return sortedMatches;
	}

	public Season aggregateSeason(String seasonStr) {
		int homeWins = 0;
		int awayWins = 0;
		int draws = 0;
		int goallessDraws = 0;
		Map<String, Season.RefereeResults> refereeMap = new HashMap<>();
		Map<String, Integer> teamPlayed = new HashMap<>();
		Map<String, Integer> teamPoints = new HashMap<>();
		Map<String, Integer> teamGoalDiff = new HashMap<>();
		log.fine("Match count = "+matchRepository.findBySeason(seasonStr).size());
		for ( Match match : matchRepository.findBySeason(seasonStr) ) {
			addResults(teamPlayed, teamPoints, teamGoalDiff, match.getAwayTeam(), match.getFullTimeAwayGoals()- match.getFullTimeHomeGoals());
			addResults(teamPlayed, teamPoints, teamGoalDiff, match.getHomeTeam(), match.getFullTimeHomeGoals()- match.getFullTimeAwayGoals());
			Integer[] addToReferee = new Integer[] {0,0,0,0};
			if (match.getFullTimeHomeGoals() > match.getFullTimeAwayGoals()) {
				homeWins++;
				addToReferee[0]++;
			}
			if (match.getFullTimeHomeGoals() < match.getFullTimeAwayGoals()) {
				awayWins++;
				addToReferee[1]++;
			}
			if (match.getFullTimeHomeGoals() == match.getFullTimeAwayGoals()) {
				draws++;
				addToReferee[2]++;
				if (match.getFullTimeHomeGoals() == 0) {
					goallessDraws++;
					addToReferee[3]++;
				}
			}
			adjustRefereeStatistics(refereeMap, match.getReferee(), addToReferee);
		}
		// figure out results. this is a pain because ties on point go to goal difference
		List<Season.TeamResult> teamResults = new ArrayList<>();
		for (Map.Entry<String, Integer> team : teamPlayed.entrySet()) {
			String teamName = team.getKey();
			Integer played = team.getValue();
			Integer points = teamPoints.getOrDefault(teamName, -1);
			Integer goalDiff = teamGoalDiff.getOrDefault(teamName, -1);
			teamResults.add(new Season.TeamResult(teamName, played, points, goalDiff));
		}
		teamResults.sort(new Season.SortByPointsAndGoalDiff());
		log.fine(String.format("EPL played : %s", matchRepository.findAll().size()));

		// stick stuff in map
		Season season = new Season();
		season.setSeason(seasonStr);
		season.setTeamResults(teamResults);
		season.setHomeWins(homeWins);
		season.setAwayWins(awayWins);
		season.setDraws(draws);
		season.setGoallessDraws(goallessDraws);
		season.setRefereeResults(refereeMap.values());
		return season;
	}

	private void adjustRefereeStatistics(Map<String, Season.RefereeResults> refereeMap, String referee, Integer[] newStats) {
		// verify entry for referee exists
		if (!refereeMap.containsKey(referee)) {
			refereeMap.put(referee, new Season.RefereeResults(referee));
		}
		Season.RefereeResults stats = refereeMap.get(referee);
		stats.setHomeWins(stats.getHomeWins()+ newStats[0]);
		stats.setAwayWins(stats.getAwayWins()+ newStats[1]);
		stats.setDraws(stats.getDraws()+ newStats[2]);
		stats.setGoallessDraws(stats.getGoallessDraws()+ newStats[3]);
	}

	private void addResults(Map<String, Integer> played, Map<String, Integer> points, Map<String, Integer> goalDiff, String team, int diff) {
		// verify team exists in maps
		if (!played.containsKey(team)) {
			played.put(team, 0);
		}
		if (!points.containsKey(team)) {
			points.put(team, 0);
		}
		if (!goalDiff.containsKey(team)) {
			goalDiff.put(team, 0);
		}
		played.put(team, 1+played.get(team));
		if (diff < 0) {
			// no points for you
		} else if (diff > 0) {
			// a win
			points.put(team, 3+points.get(team));
		} else {
			// a draw
			points.put(team, 1+points.get(team));
		}
		goalDiff.put(team, diff+goalDiff.get(team));
	}

}
