package liveproject.webreport.match;

import liveproject.webreport.season.Season;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceTest {
    public static final String SEASON_STR = "1910-1911";
    public static final String WINSTON_CHURCHILL = "Winston Churchill";
    public static final String LEEDS_UNITED = "Leeds United";
    public static final String MANCHESTER_UNITED = "Manchester United";
    public static final String LIVERPOOL = "Liverpool";
    public static final String ASTON_VILLA = "Aston Villa";
    private final MatchRepository matchRepository = Mockito.mock(MatchRepository.class);
    private MatchService matchService;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.dd.MM");

    private Match theFirstGame;
    private Match theSecondGame;

    @BeforeEach
    public void setup() throws ParseException {
        matchService = new MatchService(matchRepository);
        theFirstGame = Match.builder()
                .id(1L)
                .gameDate(sdf.parse("1910.15.08"))
                .awayTeam(MANCHESTER_UNITED)
                .homeTeam(LEEDS_UNITED)
                .fullTimeAwayGoals(0)
                .fullTimeHomeGoals(7)
                .halfTimeAwayGoals(0)
                .halfTimeHomeGoals(4)
                .referee(WINSTON_CHURCHILL)
                .fullTimeResult('H')
                .season(SEASON_STR)
                .build();
        theSecondGame = Match.builder()
                .id(1L)
                .gameDate(sdf.parse("1910.18.08"))
                .awayTeam(LIVERPOOL)
                .homeTeam(ASTON_VILLA)
                .fullTimeAwayGoals(5)
                .fullTimeHomeGoals(0)
                .halfTimeAwayGoals(4)
                .halfTimeHomeGoals(0)
                .referee(WINSTON_CHURCHILL)
                .fullTimeResult('H')
                .season(SEASON_STR)
                .build();
        when(matchRepository.findBySeason(SEASON_STR)).thenReturn(Arrays.asList(theFirstGame, theSecondGame));
    }

    @Test
    public void test_aggregateSeason() {
        Season season = matchService.aggregateSeason(SEASON_STR);
        assertThat(season.getAwayWins()).isEqualTo(1);
        assertThat(season.getHomeWins()).isEqualTo(1);
        assertThat(season.getRefereeResults().size()).isEqualTo(1);
        Season.RefereeResults refereeResults = (Season.RefereeResults) season.getRefereeResults().toArray()[0];
        assertThat(refereeResults.getName()).isEqualTo(WINSTON_CHURCHILL);
        assertThat(refereeResults.getHomeWins()).isEqualTo(1);
        assertThat(refereeResults.getAwayWins()).isEqualTo(1);
    }

    @Test
    public void test_getMatches_normal() {
        Set<Match> matches = matchService.getAllBySeasonSorted(SEASON_STR);
        assertThat(matches.size()).isEqualTo(2);
        Match match = (Match)(matches.toArray()[0]);
        assertThat(match.getHomeTeam()).isEqualTo(LEEDS_UNITED);
    }

    @Test
    public void test_getMatches_sameDay() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(date);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(date);
        theSecondGame.setGameTime("1100");
        Set<Match> matches = matchService.getAllBySeasonSorted(SEASON_STR);
        assertThat(matches.size()).isEqualTo(2);
        Match match = (Match)(matches.toArray()[0]);
        assertThat(match.getHomeTeam()).isEqualTo(ASTON_VILLA);
    }
}
