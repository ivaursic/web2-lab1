package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.lab1.domain.Match;

public interface MatchRepository extends JpaRepository<Match, Long> {
    //Match findById(Long matchID);
}
