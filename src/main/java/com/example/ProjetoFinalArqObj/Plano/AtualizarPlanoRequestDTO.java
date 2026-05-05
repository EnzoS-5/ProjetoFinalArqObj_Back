package com.example.ProjetoFinalArqObj.Plano;

import java.util.List;

public record AtualizarPlanoRequestDTO(
        String titulo,
        String descricao,
        List<Integer> habitoIds
) {
}
