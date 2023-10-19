package web2.lab1.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class UserAccount {

    @Id
    @GeneratedValue
    private Long idUser;

    @Column(unique = true)
    private String username;

    @Column(unique = true)
    private String email;

}
