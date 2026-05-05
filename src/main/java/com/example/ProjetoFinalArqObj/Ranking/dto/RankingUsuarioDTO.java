package com.example.ProjetoFinalArqObj.Ranking.dto;

import com.example.ProjetoFinalArqObj.User.User;

public record RankingUsuarioDTO(
        int posicao,
        Integer userId,
        String nome,
        int xp,
        int streak,
        int nivel
) {
    public static RankingUsuarioDTO of(User user, int posicao) {
        int nivelCalculado = user.getXp() / 1000;
        return new RankingUsuarioDTO(
                posicao,
                user.getId(),
                user.getNome(),
                user.getXp(),
                user.getStreak(),
                nivelCalculado
        );
    }
}
