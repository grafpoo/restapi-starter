package liveproject.webreport.season;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    private List<TeamResult> teamResults;
    private Integer homeWins;
    private Integer awayWins;
    private Integer draws;
    private Integer goallessDraws;
    private String season;
    private Collection<RefereeResults> refereeResults;

    // thymeleaf seems to want real getters, not lombok ones

    public List<TeamResult> getTeamResults() {
        return teamResults;
    }

    public Integer getHomeWins() {
        return homeWins;
    }

    public Integer getAwayWins() {
        return awayWins;
    }

    public Integer getDraws() {
        return draws;
    }

    public Integer getGoallessDraws() {
        return goallessDraws;
    }

    public String getSeason() {
        return season;
    }

    public Collection<RefereeResults> getRefereeResults() {
        return refereeResults;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TeamResult {
        private String teamName;
        private Integer played;
        private Integer points;
        private Integer goalDiff;

        public String getTeamName() {
            return teamName;
        }

        public Integer getPlayed() {
            return played;
        }

        public Integer getPoints() {
            return points;
        }

        public Integer getGoalDiff() {
            return goalDiff;
        }
    }

    public static class SortByPointsAndGoalDiff implements Comparator<TeamResult> {
        // descending sort (so less-than is greater-than
        public int compare(TeamResult a, TeamResult b) {
            if (a == null) return 1;
            if (b == null) return 1;
            if (a.points < b.points) return 1;
            if (a.points > b.points) return -1;
            if (a.goalDiff < b.goalDiff) return 1;
            if (a.goalDiff > b.goalDiff) return -1;
            return 0;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefereeResults {
        String name = "Nobody";
        Integer homeWins = 0;
        Integer awayWins = 0;
        Integer draws = 0;
        Integer goallessDraws = 0;

        public RefereeResults(String referee) {
            this.name = referee;
        }

        public String getName() {
            return name;
        }

        public Integer getHomeWins() {
            return homeWins;
        }

        public Integer getAwayWins() {
            return awayWins;
        }

        public Integer getDraws() {
            return draws;
        }

        public Integer getGoallessDraws() {
            return goallessDraws;
        }

        @Override
        public String toString() {
            return String.format("Referee: %s %s/%s/%s/%s",name,homeWins,awayWins,draws,goallessDraws);
        }
    }

}
