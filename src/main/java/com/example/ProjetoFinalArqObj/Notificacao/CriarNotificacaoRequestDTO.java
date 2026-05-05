package com.example.ProjetoFinalArqObj.Notificacao;

import java.time.LocalDate;

public record CriarNotificacaoRequestDTO(
        Integer habitoId,
        LocalDate dataReferencia
) {
}
