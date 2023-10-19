package web2.lab1.service;

import jakarta.transaction.Transactional;
import web2.lab1.domain.Competition;
import web2.lab1.domain.Schedule;
import web2.lab1.domain.Team;

import java.util.List;

public interface CompetitionService {

    //Competition updateCompetition(Competition competition);

    Competition createCompetition(String competitionName, String competitors, String scoringSystem, String name);

    Schedule generateSchedule(List<Team> teams, Competition competition);

    Competition findByLink(String link);

    @Transactional
    void updateCompetitionResults(Competition competition);
}
