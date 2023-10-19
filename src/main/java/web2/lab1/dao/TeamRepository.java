package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.lab1.domain.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByTeamName(String teamName);
}
