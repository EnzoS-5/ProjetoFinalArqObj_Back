package com.example.ProjetoFinalArqObj.Plano;

import java.util.List;

public record CriarPlanoRequestDTO(
        String titulo,
        String descricao,
        List<Integer> habitoIds,
        Integer metaId
) {
}
