package com.example.ProjetoFinalArqObj.Meta;

import java.time.LocalDate;

public record MetaResponseDTO(
        Integer id,
        Integer userId,
        String titulo,
        String descricao,
        LocalDate dataLimite,
        boolean concluido,
        boolean ativo
) {
    public static MetaResponseDTO of(Meta meta) {
        return new MetaResponseDTO(
                meta.getId(),
                meta.getUser().getId(),
                meta.getTitulo(),
                meta.getDescricao(),
                meta.getDataLimite(),
                meta.isConcluido(),
                meta.isAtivo()
        );
    }
}
