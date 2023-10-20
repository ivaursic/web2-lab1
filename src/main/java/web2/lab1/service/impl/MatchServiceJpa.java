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
    public void setTeamScores(Match match) {
        String scoringSystem = match.getCompetition().getScoringSystem();
        String[] scoringValues = scoringSystem.split("/");

        double winPoints = Double.parseDouble(scoringValues[0]);
        double drawPoints = Double.parseDouble(scoringValues[1]);
        double lossPoints = Double.parseDouble(scoringValues[2]);

        Team firstTeam = match.getFirstTeam();
        Team secondTeam = match.getSecondTeam();

        int firstTeamScore = match.getFirstTeamScore();
        int secondTeamScore = match.getSecondTeamScore();

        // Ako prvi tim pobijedi
        if (firstTeamScore > secondTeamScore) {
            firstTeam.setScore(firstTeam.getScore() + winPoints);
            secondTeam.setScore(secondTeam.getScore() + lossPoints);
        }
        // Ako drugi tim pobijedi
        else if (secondTeamScore > firstTeamScore) {
            secondTeam.setScore(secondTeam.getScore() + winPoints);
            firstTeam.setScore(firstTeam.getScore() + lossPoints);
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
    @Override
    public void updateTeamScores(Match match, int firstTeamScore, int secondTeamScore) {
        String scoringSystem = match.getCompetition().getScoringSystem();
        String[] scoringValues = scoringSystem.split("/");

        double winPoints = Double.parseDouble(scoringValues[0]);
        double drawPoints = Double.parseDouble(scoringValues[1]);
        double lossPoints = Double.parseDouble(scoringValues[2]);

        // Dohvatite timove iz meča
        Team firstTeam = match.getFirstTeam();
        Team secondTeam = match.getSecondTeam();

        // Dohvatite bodove iz trenutnog rezultata meča
        int currentFirstTeamScore = match.getFirstTeamScore();
        int currentSecondTeamScore = match.getSecondTeamScore();

        int currentResult = currentFirstTeamScore - currentSecondTeamScore;
        int newResult = firstTeamScore - secondTeamScore;

        if((newResult < 0 && currentResult < 0) || (newResult == 0 && currentResult == 0) || (newResult > 0 && currentResult > 0))
            return;

        if(currentResult < 0){
            secondTeam.setScore(secondTeam.getScore() - winPoints);
            firstTeam.setScore(firstTeam.getScore() - lossPoints);
        } if(currentResult > 0){
            firstTeam.setScore(firstTeam.getScore() - winPoints);
            secondTeam.setScore(secondTeam.getScore() - lossPoints);
        } else if (currentResult == 0) {
            firstTeam.setScore(firstTeam.getScore() - drawPoints);
            secondTeam.setScore(secondTeam.getScore() - drawPoints);
        }

        if(newResult > 0){
            firstTeam.setScore(firstTeam.getScore() + winPoints);
            secondTeam.setScore(secondTeam.getScore() + lossPoints);
        } if(newResult < 0){
            secondTeam.setScore(secondTeam.getScore() + winPoints);
            firstTeam.setScore(firstTeam.getScore() + lossPoints);
        } else if (newResult == 0) {
            firstTeam.setScore(firstTeam.getScore() + drawPoints);
            secondTeam.setScore(secondTeam.getScore() + drawPoints);
        }

        // Spremi ažurirane timove u bazu podataka
        teamService.save(firstTeam);
        teamService.save(secondTeam);

        // Ako želite, možete ažurirati i rezultate u samom meču
        match.setFirstTeamScore(firstTeamScore);
        match.setSecondTeamScore(secondTeamScore);
        matchRepository.save(match);
    }


}
