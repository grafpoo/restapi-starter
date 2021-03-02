package liveproject.webreport.match;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByHomeTeam(String team);
    List<Match> findByAwayTeam(String team);
    List<Match> findByHomeTeamAndAwayTeamAndGameDate(String homeTeam, String awayTeam, Date date);
    List<Match> findBySeason(String season);
}
