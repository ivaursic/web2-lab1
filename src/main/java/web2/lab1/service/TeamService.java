package web2.lab1.service;

import web2.lab1.domain.Team;

public interface TeamService {
    Team createTeam(String teamName);

    Team getByTeamName(String teamName);

    void save(Team firstTeam);
}
