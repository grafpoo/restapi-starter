package liveproject.webreport.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Controller
public class MatchController {

    private MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("reports/season-report")
    @ResponseStatus(value = HttpStatus.OK)
    public String account(@ModelAttribute MatchForm matchForm, Model model) {
        model.addAttribute("seasons", matchService.getAllSeasons());
        if (matchForm.getSeasonSelected() != null) {
            model.addAttribute("season", matchService.aggregateSeason(matchForm.getSeasonSelected()));
        }
        return "reports/SeasonReport";
    }

    @GetMapping("reports/matches-report/{season}")
    @ResponseStatus(value = HttpStatus.OK)
    public String matchesReport(@PathVariable("season") String season, Model model) {
        Set<Match> seasonSorted = matchService.getAllBySeasonSorted(season);
        int totalCount = seasonSorted.stream()
                .mapToInt(i -> 1) // this unboxes
                .sum();
        model.addAttribute("matchesCounts", totalCount);
        model.addAttribute("matches", seasonSorted);
        return "reports/MatchesReport";
    }

    @GetMapping("/upload")
    public String setupUpload() {
        return "reports/LoadReport";
    }

    @PostMapping("/upload")
    public String addMatches(@RequestParam("file") MultipartFile file, Model model) {
        try {
            List<Match> matches = jsonArrayToObjectList(file.getBytes(), Match.class);
            String season = file.getOriginalFilename().split("\\.")[0]; // "2019-2020";
            Map<String, Integer> counts = matchService.saveAll(season, matches);
            int totalCount = counts.values().stream()
                    .mapToInt(i -> i) // this unboxes
                    .sum();
            model.addAttribute("seasonCounts", counts);
            model.addAttribute("totalCount", totalCount);
        } catch (IOException ioe) {

        }
        return "reports/LoadReport";
    }

    // from https://stackoverflow.com/a/57618096
    public <T> List<T> jsonArrayToObjectList(byte[] bytes, Class<T> tClass) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
        CollectionType listType = mapper.getTypeFactory()
                .constructCollectionType(ArrayList.class, tClass);
        return mapper.readValue(bytes, listType);
    }

}
