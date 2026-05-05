package com.example.ProjetoFinalArqObj.Mascote.dto;

import com.example.ProjetoFinalArqObj.Mascote.Mascote;
public record MascoteResponseDTO(
        Integer id,
        Integer userId,
        int hp,
        boolean check,
        boolean ativo
) {
    public static MascoteResponseDTO of(Mascote mascote) {
        return new MascoteResponseDTO(
                mascote.getId(),
                mascote.getUser().getId(),
                mascote.getHp(),
                mascote.isCheck(),
                mascote.isAtivo()
        );
    }
}
