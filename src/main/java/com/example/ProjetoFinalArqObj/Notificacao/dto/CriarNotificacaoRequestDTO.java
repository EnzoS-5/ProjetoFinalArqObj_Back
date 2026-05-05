package com.example.ProjetoFinalArqObj.Notificacao.dto;

import java.time.LocalDate;

public record CriarNotificacaoRequestDTO(
        Integer habitoId,
        LocalDate dataReferencia
) {
}
