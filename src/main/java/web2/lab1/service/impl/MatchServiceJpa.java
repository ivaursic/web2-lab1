package web2.lab1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.lab1.dao.MatchRepository;
import web2.lab1.domain.Match;
import web2.lab1.domain.Team;
import web2.lab1.service.MatchService;
import web2.lab1.service.TeamService;

import java.util.Optional;

@Service
public class MatchServiceJpa implements MatchService {

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private TeamService teamService;

    @Override
    public Match findById(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(null);
    }

    @Override
    public void save(Match match) {
        matchRepository.save(match);
    }

    @Override
    public void updateTeamScores(Match match) {
        String scoringSystem = match.getCompetition().getScoringSystem();
        String[] scoringValues = scoringSystem.split("/");

        int winPoints = Integer.parseInt(scoringValues[0]);
        int drawPoints = Integer.parseInt(scoringValues[1]);
        int lossPoints = Integer.parseInt(scoringValues[2]);

        System.out.println("win: " + winPoints);
        System.out.println("draw: " + drawPoints);
        System.out.println("loss: " + lossPoints);


        Team firstTeam = match.getFirstTeam();
        Team secondTeam = match.getSecondTeam();

        int firstTeamScore = match.getFirstTeamScore();
        int secondTeamScore = match.getSecondTeamScore();

        // Ako prvi tim pobijedi
        if (firstTeamScore > secondTeamScore) {
            firstTeam.setScore(firstTeam.getScore() + winPoints);
        }
        // Ako drugi tim pobijedi
        else if (secondTeamScore > firstTeamScore) {
            secondTeam.setScore(secondTeam.getScore() + winPoints);
        }
        // Ako je neriješeno
        else {
            firstTeam.setScore(firstTeam.getScore() + drawPoints);
            secondTeam.setScore(secondTeam.getScore() + drawPoints);
        }

        // Spremi ažurirane timove u bazu podataka
        teamService.save(firstTeam);
        teamService.save(secondTeam);
    }

}
