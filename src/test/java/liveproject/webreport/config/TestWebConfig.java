package liveproject.webreport.config;

import liveproject.webreport.match.MatchController;
import liveproject.webreport.match.MatchService;
import liveproject.webreport.season.Season;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.when;

@Configuration
public class TestWebConfig {

    public static final String SEASON_STR = "1910-1911";

    @Bean
    public MatchService profileService() {
        MatchService mock = Mockito.mock(MatchService.class);
        Season season = Season.builder()
                .season(SEASON_STR)
                .build();
        when(mock.aggregateSeason(SEASON_STR)).thenReturn(season);
        return mock;
    }

    @Bean
    public MatchController matchController() {
        return new MatchController(profileService());
    }
}
