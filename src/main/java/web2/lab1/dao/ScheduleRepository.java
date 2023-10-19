package web2.lab1.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import web2.lab1.domain.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Schedule save(Schedule schedule);
}
