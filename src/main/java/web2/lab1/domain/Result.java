package web2.lab1.domain;

import jakarta.persistence.*;

@Entity
public class Result {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;

    private int pointsEarned;

    // Getters and setters
}