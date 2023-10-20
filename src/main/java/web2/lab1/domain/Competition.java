package web2.lab1.domain;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Competition {

    @Id
    @GeneratedValue
    private Long idCompetiton;

    public Long getIdCompetiton() {
        return idCompetiton;
    }

    private String competitionName;

    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    private Schedule schedule;


    @OneToMany
    private List<Team> teams;

    @OneToMany(mappedBy = "competition", cascade = CascadeType.ALL)
    private List<Match> matches;
    private String scoringSystem;

    private String link;

    public Competition() {
        this.link = generateUniqueLink();
    }

    public Competition(String competitionName, List<Team> teams, String scoringSystem, String username) {
        this.competitionName = competitionName;
        this.teams = teams;
        this.scoringSystem = scoringSystem;
        this.username = username;
        this.link = generateUniqueLink();
    }

    public String getCompetitionName() {
        return competitionName;
    }

    public void setCompetitionName(String competitionName) {
        this.competitionName = competitionName;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public String getScoringSystem() {
        return scoringSystem;
    }

    public void setScoringSystem(String scoringSystem) {
        this.scoringSystem = scoringSystem;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    private String generateUniqueLink() {
        return UUID.randomUUID().toString();
    }


}