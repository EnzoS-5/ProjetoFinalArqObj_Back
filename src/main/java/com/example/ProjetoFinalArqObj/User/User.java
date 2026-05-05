package com.example.ProjetoFinalArqObj.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.ZoneId;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of("America/Sao_Paulo");

    @Column(nullable = false)
    private String nome;


    @Column(nullable = false)
    private int xp;

    @Column(nullable = false)
    private int maxHabitos;

    @Column(nullable = false)
    private int maxPlano;

    @Column(nullable = false)
    private int maxMetas;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false)
    private int streak;

    @Column(nullable = true)
    private LocalDate lastStreakDate;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private int nivel;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;

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
        this.nivel = 0;
        this.ativo = true;
        this.maxMetas = 1;
        this.maxPlano = 1;
        this.maxHabitos = 2;
    }

    public void aumentaStats(){
        int bonus = this.nivel / 5;
        this.maxHabitos = 2 + bonus;
        this.maxMetas = 1 + bonus;
        this.maxPlano = 1 + bonus;
    }


    public void incrementarStreak() {
        this.streak += 1;

    }


    public void resetarStreak() {
        this.streak = 0;
    }


    public void adicionarXp(int valor) {
        int mult = this.streak/5;
        if (mult > 0){
            valor *= 2*mult;
        }
        this.xp += valor;
        atualizarNivelELimites();
    }

    public boolean podeAumentarStreakHoje() {
        LocalDate hoje = LocalDate.now(SAO_PAULO_ZONE);
        return lastStreakDate == null || !lastStreakDate.equals(hoje);
    }

    public void incrementarStreakComData() {
        this.streak += 1;
        this.lastStreakDate = LocalDate.now(SAO_PAULO_ZONE);
    }

    public int montarNivel(){
        return this.xp / 1000;
    }

    private void atualizarNivelELimites() {
        this.nivel = montarNivel();
        aumentaStats();
    }
}
