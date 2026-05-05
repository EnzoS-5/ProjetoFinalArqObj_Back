package com.example.ProjetoFinalArqObj.Plano.dto;

import com.example.ProjetoFinalArqObj.Plano.Plano;
import java.time.LocalDateTime;
import java.util.List;

public record PlanoResponseDTO(
        Integer id,
        Integer userId,
        String titulo,
        String descricao,
        List<Integer> habitoIds,
        Integer metaId,
        boolean concluido,
        boolean ativo,
        LocalDateTime dataAtualizacao
) {
    public static PlanoResponseDTO of(Plano plano) {
        return new PlanoResponseDTO(
                plano.getId(),
                plano.getUser().getId(),
                plano.getTitulo(),
                plano.getDescricao(),
                plano.getHabitos() == null ? List.of() : plano.getHabitos().stream().map(habito -> habito.getId()).toList(),
                plano.getMeta() == null ? null : plano.getMeta().getId(),
                plano.isConcluido(),
                plano.isAtivo(),
                plano.getDataAtualizacao()
        );
    }
}
