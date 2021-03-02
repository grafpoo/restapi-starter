package liveproject.webreport.match;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

public class MatchTest {
    public static final String SEASON_STR = "1910-1911";
    public static final String WINSTON_CHURCHILL = "Winston Churchill";
    public static final String LEEDS_UNITED = "Leeds United";
    public static final String MANCHESTER_UNITED = "Manchester United";
    public static final String LIVERPOOL = "Liverpool";
    public static final String ASTON_VILLA = "Aston Villa";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.dd.MM");

    private Match theFirstGame;
    private Match theSecondGame;

    @BeforeEach
    public void setup() throws ParseException {
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
    }

    @Test
    public void test_compare_differentDate() throws ParseException {
        assertThat(theFirstGame.compareTo(theSecondGame)).isLessThan(0);
    }

    @Test
    public void test_compare_sameDate() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(date);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(date);
        theSecondGame.setGameTime("1100");
        assertThat(theFirstGame.compareTo(theSecondGame)).isGreaterThan(0);
    }

    @Test
    public void test_getMatches_dateNull() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(null);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(date);
        theSecondGame.setGameTime("1100");
        assertThat(theFirstGame.compareTo(theSecondGame)).isGreaterThan(0);
    }

    @Test
    public void test_getMatches_otherDateNull() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(date);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(null);
        theSecondGame.setGameTime("1100");
        assertThat(theFirstGame.compareTo(theSecondGame)).isLessThan(0);
    }

    @Test
    public void test_getMatches_bothDatesNull_normalTimes() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(null);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(null);
        theSecondGame.setGameTime("1100");
        assertThat(theFirstGame.compareTo(theSecondGame)).isGreaterThan(0);
    }

    @Test
    public void test_getMatches_bothDatesNull_nullTime() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(null);
        theFirstGame.setGameTime(null);
        theSecondGame.setGameDate(null);
        theSecondGame.setGameTime("1100");
        assertThat(theFirstGame.compareTo(theSecondGame)).isGreaterThan(0);
    }

    @Test
    public void test_getMatches_bothDatesNull_otherNullTime() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(null);
        theFirstGame.setGameTime("1230");
        theSecondGame.setGameDate(null);
        theSecondGame.setGameTime(null);
        assertThat(theFirstGame.compareTo(theSecondGame)).isLessThan(0);
    }

    @Test
    public void test_getMatches_bothDatesNull_bothTimesNull() throws ParseException {
        Date date = sdf.parse("1910.15.08");
        theFirstGame.setGameDate(null);
        theFirstGame.setGameTime(null);
        theSecondGame.setGameDate(null);
        theSecondGame.setGameTime(null);
        assertThat(theFirstGame.compareTo(theSecondGame)).isGreaterThan(0);
    }

}
