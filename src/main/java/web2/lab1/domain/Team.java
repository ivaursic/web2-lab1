package web2.lab1.domain;

import jakarta.persistence.*;

@Entity
public class Team {

    @Id
    @GeneratedValue
    private Long idTeam;

    private String teamName;

    @ManyToOne
    private Competition competition;

    private double score = 0;

    public Team() {
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public Long getId() {
        return idTeam;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
