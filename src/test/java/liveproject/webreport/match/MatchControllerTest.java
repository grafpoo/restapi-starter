package liveproject.webreport.match;

import liveproject.webreport.config.TestWebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static liveproject.webreport.config.TestWebConfig.SEASON_STR;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestWebConfig.class})
@WebAppConfiguration
public class MatchControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private MatchController controller;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  public void find_shouldAddSeasonToModelAndRenderSeasonReportView() throws Exception {
    mockMvc.perform(get("/reports/season-report")
    		.param("seasonSelected", SEASON_STR))
            .andExpect(status().isOk())
            .andExpect(view().name("reports/SeasonReport"))
            .andExpect(forwardedUrl("reports/SeasonReport"))
            .andExpect(model().attribute("season", hasProperty("season", is(SEASON_STR))));
  }

  @Test
  public void find_shouldAddMatchesToModelAndRenderMatchReportView() throws Exception {
    mockMvc.perform(get("/reports/matches-report/"+SEASON_STR))
            .andExpect(status().isOk())
            .andExpect(view().name("reports/MatchesReport"))
            .andExpect(forwardedUrl("reports/MatchesReport"))
            .andExpect(model().attribute("matches", instanceOf(Set.class)));
  }

  @Test
  public void load_shouldLoadAndRenderSeasonReportView() throws Exception {
	  
   MockMultipartFile file = new MockMultipartFile(
		   "file", 
		   "seasons.json", 
		   MediaType.TEXT_PLAIN_VALUE, 
		   "[{\"division\":\"E0\",\"gameDate\":\"09/08/2019\",\"gameTime\":\"20:00\",\"homeTeam\":\"Liverpool\",\"awayTeam\":\"Norwich\"}]".getBytes()
		   );  
	  
    mockMvc.perform(multipart("/upload").file(file))
            .andExpect(status().isOk())
            .andExpect(view().name("reports/LoadReport"))
            .andExpect(forwardedUrl("reports/LoadReport"));
  }
}
