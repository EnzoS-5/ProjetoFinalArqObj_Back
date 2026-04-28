package com.example.ProjetoFinalArqObj.User;


public class User {

    private String nome;
    private int xp;
    private String id;
    private int streak;
    private String email;
    private String senha;

    public User(String nome, String email, String senha, String id){
        this.email = email;
        this.nome = nome;
        this.senha = senha;
        this.xp = 0;
        this.streak = 0;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak() {
        this.streak += 1;
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

}
