package web2.lab1.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import web2.lab1.dao.TeamRepository;
import web2.lab1.domain.Team;
import web2.lab1.service.TeamService;

@Service
public class TeamServiceJpa implements TeamService {

    @Autowired
    private TeamRepository teamRepository;


    @Override
    public Team createTeam(String teamName) {
        Team existingTeam = teamRepository.findByTeamName(teamName);
        if (existingTeam != null) {
            // Tim s tim imenom već postoji, ne želimo stvoriti novi tim
            return existingTeam;
        }
        // Stvori novi tim i spremi ga u bazu
        return teamRepository.save(new Team(teamName));
    }

    @Override
    public Team getByTeamName(String teamName) {
        return null;
    }

    @Override
    public void save(Team team) {
        teamRepository.save(team);
    }
}
