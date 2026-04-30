package com.example.ProjetoFinalArqObj.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User {

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int xp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private int streak;

    @Column(nullable = false, unique = true)
    private String email;

    @JsonIgnore
    private String senha;

    public User(String nome, String email, String senha, Integer id){
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.xp = 0;
        this.streak = 0;
        this.id = id;
    }

    public void setStreak() {
        this.streak += 1;
    }

}
