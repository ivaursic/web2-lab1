package web2.lab1.service;

import web2.lab1.domain.Match;

import java.util.Optional;

public interface MatchService {
    Match findById(Long matchId);

    void save(Match match);

    void setTeamScores(Match match);

    public void updateTeamScores(Match match, int firtTeamScore, int secondTeamScore);
}
