package web2.lab1.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.lab1.dao.CompetitionRepository;
import web2.lab1.dao.MatchRepository;
import web2.lab1.dao.ScheduleRepository;
import web2.lab1.domain.Competition;
import web2.lab1.domain.Match;
import web2.lab1.domain.Schedule;
import web2.lab1.domain.Team;
import web2.lab1.service.CompetitionService;
import web2.lab1.service.TeamService;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompetitionServiceJpa implements CompetitionService {

    @Autowired
    private CompetitionRepository competitionRepository;

    @Autowired
    private TeamService teamService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private MatchRepository matchRepository;

    @Override
    @Transactional
    public Competition createCompetition(String competitionName, String competitors, String scoringSystem, String username) {
        System.out.println("uslo u service");
        Competition competition = new Competition();
        competition.setCompetitionName(competitionName);
        competition.setScoringSystem(scoringSystem);
        competition.setUsername(username);

        competition = competitionRepository.save(competition); // Prvo spremite natjecanje kako biste dobili ID

        List<Team> teams = new ArrayList<>();
        for (String teamName : competitors.split("[;,\\r?\\n]+")) {
            Team team = new Team(teamName);
            team.setCompetition(competition); // Povežite tim s natjecanjem
            teams.add(teamService.createTeam(teamName));
        }

        competition.setTeams(teams);
        System.out.println("Competition Name: " + competition.getCompetitionName());
        System.out.println("Scoring System: " + competition.getScoringSystem());
        System.out.println("Number of Teams: " + competition.getTeams().size());

        System.out.println("Team IDs:");
        for (Team team : competition.getTeams()) {
            System.out.println(team.getId()); // Ispisivanje ID-a svakog tima
        }

        return competitionRepository.save(competition);
    }

    @Override
    public Schedule generateSchedule(List<Team> teams, Competition competition) {
        Schedule schedule = new Schedule();
        schedule.setCompetition(competition);

        boolean neparno = false;

        List<Match> matches = new ArrayList<>();


        int numTeams = teams.size();
        int numRounds = numTeams - 1;
        int matchNumber = 1;

        if (numTeams % 2 != 0) {
            neparno = true;
            numRounds++; // Ako je broj timova neparan, dodaj još jednu rundu za slobodan tim
            numTeams++;
            teams.add(new Team("Slobodan")); // Dodaj slobodan tim ako je broj timova neparan
        }

            // Generišite raspored za svaku rundu
        for (int round = 1; round <= numRounds; round++) {
            for (int i = 0; i < numTeams / 2; i++) {
                int team1Index = i;
                int team2Index = numTeams - 1 - i;

                Team team1 = teams.get(team1Index);
                Team team2 = teams.get(team2Index);

                System.out.println("tim1: " + team1.getTeamName());
                System.out.println("tim2: " + team2.getTeamName());

                if(neparno && (team1.getTeamName().equals("Slobodan") || team2.getTeamName().equals("Slobodan"))){
                    System.out.println("tim1 neparno : " + team1.getTeamName());
                    System.out.println("tim2 neparno : " + team2.getTeamName());
                    Match match = new Match();
                    match.setRoundNumber(round);
                    if(team1.getTeamName().equals("Slobodan")){
                        match.setFirstTeam(team2);
                        //drugi tim slobodan
                    } else if (team2.getTeamName().equals("Slobodan")) {
                        match.setFirstTeam(team1);
                        //drugi tim slobodan
                    }
                    match.setMatchNumber(matchNumber++);
                    match.setCompetition(competition);
                    matches.add(match);
                    continue;
                }

                Match match = new Match();
                match.setMatchNumber(matchNumber++);
                match.setRoundNumber(round);
                match.setFirstTeam(team1);
                match.setSecondTeam(team2);
                match.setCompetition(competition);
                matches.add(match);

            }

            // Rotacija timova osim prvog tima
            Team lastTeam = teams.get(numTeams - 1);
            for (int i = numTeams - 1; i > 1; i--) {
                teams.set(i, teams.get(i - 1));
            }
            teams.set(1, lastTeam);
        }

        // Ako je broj timova neparan, izbaci "Slobodan" tim iz liste nakon generiranja rasporeda
        if (neparno) {
            teams.removeIf(team -> team.getTeamName().equals("Slobodan"));
        }

        competition.setMatches(matches);
        competitionRepository.save(competition);

        // Spremite mečeve u bazu
        matchRepository.saveAll(matches);

        // Spremite mečeve u raspored
        schedule.setMatches(matches);

        schedule.setRoundNumber(numRounds);

        // Spremite raspored u bazu
        schedule = scheduleRepository.save(schedule);

        competition.setSchedule(schedule);
        competitionRepository.save(competition);

        return schedule;
    }

    @Override
    public Competition findByLink(String link) {
        return competitionRepository.findByLink(link);
    }

    @Transactional
    @Override
    public void updateCompetitionResults(Competition competition) {
        // Ažurirajte rezultate natjecanja u bazi podataka
        competitionRepository.save(competition);
    }

}

