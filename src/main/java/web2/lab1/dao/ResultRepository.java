package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.lab1.domain.Result;

public interface ResultRepository extends JpaRepository<Result, Long> {
}
