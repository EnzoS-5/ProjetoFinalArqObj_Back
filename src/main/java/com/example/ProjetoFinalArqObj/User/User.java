package com.example.ProjetoFinalArqObj.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "users")
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

    @Column(nullable = true)
    private LocalDate lastStreakDate;

    @Column(nullable = false, unique = true)
    private String email;


    @JsonIgnore
    private String senha;


    protected User() {}

    public User(String nome, String email, String senha){
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.xp = 0;
        this.streak = 0;
        this.lastStreakDate = null;
    }


    public void incrementarStreak() {
        this.streak += 1;
    }


    public void resetarStreak() {
        this.streak = 0;
    }


    public void adicionarXp(int valor) {
        this.xp += valor;
    }

    public boolean podeAumentarStreakHoje() {
        LocalDate hoje = LocalDate.now();
        return lastStreakDate == null || !lastStreakDate.equals(hoje);
    }

    public void incrementarStreakComData() {
        this.streak += 1;
        this.lastStreakDate = LocalDate.now();
    }
}
