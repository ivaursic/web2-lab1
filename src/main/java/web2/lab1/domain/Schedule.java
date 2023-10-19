package web2.lab1.domain;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Schedule {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    //@JoinColumn(name = "idCompetition")
    private Competition competition;

    private int roundNumber;

    @OneToMany
    private List<Match> matches;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public Competition getCompetition() {
        return competition;
    }

    public void setCompetition(Competition competition) {
        this.competition = competition;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }
}