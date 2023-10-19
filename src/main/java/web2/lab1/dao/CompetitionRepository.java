package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.lab1.domain.Competition;

public interface CompetitionRepository extends JpaRepository<Competition, Long> {
    Competition findByLink(String link);
}
