package liveproject.webreport.match;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table( name = "EPL" )
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Match implements Serializable, Comparable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    private String division;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yy") private Date gameDate;
    private String gameTime;
    private String homeTeam;
    private String awayTeam;
    private Integer fullTimeHomeGoals;
    private Integer fullTimeAwayGoals;
    private Character fullTimeResult;
    private Integer halfTimeHomeGoals;
    private Integer halfTimeAwayGoals;
    private Character halfTimeResult;
    private String referee;
    private Integer homeTeamShots;
    private Integer awayTeamShots;
    private Integer homeTeamShotsOnTarget;
    private Integer awayTeamShotsOnTarget;
    private Integer homeTeamFouls;
    private Integer awayTeamFouls;
    private Integer homeTeamCorners;
    private Integer awayTeamCorners;
    private Integer homeTeamYellowCards;
    private Integer awayTeamYellowCards;
    private Integer homeTeamRedCards;
    private Integer awayTeamRedCards;
    private String season;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        if (gameDate == null || homeTeam == null || awayTeam == null) return false;
        return gameDate.equals(match.gameDate) &&
                homeTeam.equals(match.homeTeam) &&
                awayTeam.equals(match.awayTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameDate, homeTeam, awayTeam);
    }

    @Override
    public int compareTo(Object o) {
        if (! (o instanceof Match)) return 1;
        Match omatch = (Match) o;
        if (gameDate == null) {
            // if mine is null and other isn't, i come after
            if (omatch.gameDate != null) return 1;
        } else {
            if (omatch.gameDate == null) return -1;
            int dateCompare = gameDate.compareTo(omatch.gameDate);
            if (dateCompare != 0) return dateCompare;
        }
        if (gameTime == null) {
            // if mine is null and other isn't, i come after
            if (omatch.gameTime != null) return 1;
        } else {
            if (omatch.gameTime == null) return -1;
            int timeCompare = gameTime.compareTo(omatch.gameTime);
            if (timeCompare != 0) return timeCompare;
        }
        return homeTeam.compareTo(omatch.homeTeam);
    }
}
