package web2.lab1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import web2.lab1.domain.Competition;
import web2.lab1.domain.Match;
import web2.lab1.domain.Schedule;
import web2.lab1.domain.Team;
import web2.lab1.service.CompetitionService;
import web2.lab1.service.MatchService;
import web2.lab1.service.TeamService;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@Controller
public class CompetitionController {

    @Autowired
    private CompetitionService competitionService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private MatchService matchService;

    @GetMapping("/competitionForm")
    public String showCompetitionForm(Model model) {
        model.addAttribute("competition", new Competition());
        return "competitionForm";
    }

    @RequestMapping(value = "/createCompetition", method = RequestMethod.POST)
    public String createCompetition(@RequestParam("competitionName") String competitionName,
                                    @RequestParam("competitors") String competitors,
                                    @RequestParam("scoringSystem") String scoringSystem,
                                    Principal principal, Model model) {

        String name = "pocetno";
        if (principal != null) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            System.out.println("get name: " + oauth2User.getAttributes());
            name = oauth2User.getAttribute("name"); // Dohvatite korisničko ime iz atributa "name"
        } else {
            return "redirect:/error";
        }

        Competition competition = competitionService.createCompetition(competitionName, competitors, scoringSystem, name);
        model.addAttribute("competition", competition);
        Schedule schedule = competitionService.generateSchedule(competition.getTeams(), competition);
        model.addAttribute(schedule);
        List<Match> matches = competition.getMatches();
        Collections.sort(matches, Comparator.comparing(Match::getRoundNumber).thenComparing(Match::getMatchNumber));
        model.addAttribute("matches", matches);
        return "createdCompetition";
    }

    @GetMapping("/user-info")
    public String getUserInfo(Principal principal, Model model) {
        if (principal != null) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String email = oauth2User.getAttribute("email"); // Dohvatite korisničko ime iz atributa "email"
            String name = oauth2User.getAttribute("name");   // Dohvatite korisničko ime iz atributa "name"

            // Dodajte korisničko ime u model kako biste ga mogli prikazati na stranici
            model.addAttribute("email", email);
            model.addAttribute("name", name);

            oauthToken.setAuthenticated(false);
        }
        return "user-info"; // Prikazuje stranicu sa podacima o korisniku
    }

    @GetMapping("/competition/{link}")
    public String showCompetitionDetails(@PathVariable String link, Model model) {
        Competition competition = competitionService.findByLink(link);

        Schedule schedule = competition.getSchedule();
        if (schedule != null) {
            List<Match> matches = schedule.getMatches();
            for (Match match : matches) {
                System.out.println("Round Number: " + match.getRoundNumber());
                System.out.println("Team 1: " + match.getFirstTeam().getTeamName());
                System.out.println("Team 2: " + (match.getSecondTeam() != null ? match.getSecondTeam().getTeamName() : "Slobodan"));
                System.out.println("Result: " + match.getFirstTeamScore() + " - " + match.getSecondTeamScore());
            }
        }
        System.out.println("Schedule Round Number: " + schedule.getRoundNumber());
        model.addAttribute("competition", competition);
        model.addAttribute("schedule", schedule);
        List<Match> matches = competition.getMatches();
        Collections.sort(matches, Comparator.comparing(Match::getRoundNumber).thenComparing(Match::getMatchNumber));
        model.addAttribute("matches", matches);
        // Dohvati timove i sortiraj ih po bodovima
        List<Team> teams = competition.getTeams();
        teams.sort(Comparator.comparingDouble(Team::getScore).reversed());
        model.addAttribute("teams", teams);
        return "competitionDetails"; // Ovo je ime Thymeleaf templatea za prikazivanje detalja natjecanja
    }

    @PostMapping("/competition/{link}/submit-result")
    public String submitResult(@PathVariable String link,
                               @RequestParam("matchId") Long matchId,
                               @RequestParam("firstTeamScore") int firstTeamScore,
                               @RequestParam("secondTeamScore") int secondTeamScore,
                               Model model) {
        // Dohvatite utakmicu iz baze podataka prema matchId
        Match match = matchService.findById(matchId);

        // Ažurirajte rezultat utakmice
        match.setFirstTeamScore(firstTeamScore);

        match.setSecondTeamScore(secondTeamScore);

        // Spremite ažuriranu utakmicu u bazu podataka
        matchService.save(match);

        matchService.setTeamScores(match);

        // Ponovno dohvatite sve utakmice za prikaz na stranici
        Competition competition = match.getCompetition();
        List<Match> matches = competition.getMatches();

        // Sortirajte utakmice po rundama i brojevima mečeva
        Collections.sort(matches, Comparator.comparing(Match::getRoundNumber).thenComparing(Match::getMatchNumber));

        // Dodajte ažurirane utakmice u model
        model.addAttribute("competition", competition);
        model.addAttribute("schedule", competition.getSchedule());
        model.addAttribute("matches", matches);

        // Preusmjerite korisnika na stranicu s rasporedom
        return "createdCompetition";
    }

    @GetMapping("/competition/{link}/edit-result/{matchId}")
    public String showEditMatchForm(@PathVariable String link, @PathVariable Long matchId, Model model) {
        // Dohvatite meč iz baze podataka prema matchId i prosljeđujte ga na formu za uređivanje
        Match match = matchService.findById(matchId);
        Competition competition = match.getCompetition();
        model.addAttribute("competition", competition);
        model.addAttribute("match", match);
        return "editMatchForm"; // Zamijenajte "editMatchForm" s nazivom vaše Thymeleaf stranice za uređivanje mečeva
    }

    @PostMapping("/competition/{link}/edit-result/{matchId}")
    public String editMatchResult(@PathVariable String link, @PathVariable Long matchId,
                                  @RequestParam("firstTeamScore") int firstTeamScore,
                                  @RequestParam("secondTeamScore") int secondTeamScore,
                                  Model model) {
        // Dohvatite meč iz baze podataka prema matchId
        Match match = matchService.findById(matchId);


        // Ponovno dohvatite sve utakmice za prikaz na stranici
        Competition competition = match.getCompetition();
        List<Match> matches = competition.getMatches();

        matchService.updateTeamScores(match, firstTeamScore, secondTeamScore);

        // Sortirajte utakmice po rundama i brojevima mečeva
        Collections.sort(matches, Comparator.comparing(Match::getRoundNumber).thenComparing(Match::getMatchNumber));

        // Dodajte ažurirane utakmice u model
        model.addAttribute("competition", competition);
        model.addAttribute("schedule", competition.getSchedule());
        model.addAttribute("matches", matches);

        // Preusmjerite korisnika na stranicu s rasporedom
        return "createdCompetition";
    }

    @GetMapping("/my-competitions")
    public String showUserCompetitions(Principal principal, Model model) {
        if (principal != null) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            OAuth2User oauth2User = oauthToken.getPrincipal();
            String username = oauth2User.getAttribute("name"); // Dohvatite korisničko ime iz atributa "name"

            List<Competition> userCompetitions = competitionService.getCompetitionsByUsername(username);
            model.addAttribute("competitions", userCompetitions);

            return "myCompetitions"; // Ovo je ime Thymeleaf templatea za prikazivanje natjecanja korisnika
        }
        return "redirect:/error"; // Preusmjeri na grešku ako korisnik nije prijavljen
    }

    @GetMapping("/competition/{id}/edit")
    public String showEditCompetitionForm(@PathVariable Long id, Model model) {
        // Dohvatite natjecanje iz baze podataka prema ID-u i prosljeđujte ga na formu za uređivanje
        Competition competition = competitionService.findById(id);
        Schedule schedule = competition.getSchedule();
        model.addAttribute("schedule", schedule);
        List<Match> matches = competition.getMatches();
        Collections.sort(matches, Comparator.comparing(Match::getRoundNumber).thenComparing(Match::getMatchNumber));
        model.addAttribute("matches", matches);
        model.addAttribute("competition", competition);
        return "createdCompetition"; // Zamijenajte ovo s nazivom vaše Thymeleaf stranice za uređivanje natjecanja
    }

    @RequestMapping("/my-logout")
    public String logout(Principal principal) {
        if (principal != null) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) principal;
            oauthToken.setAuthenticated(false);
        }
        return "redirect:/logout";
    }



}
