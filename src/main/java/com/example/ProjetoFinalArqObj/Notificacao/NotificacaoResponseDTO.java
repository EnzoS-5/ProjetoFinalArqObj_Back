package com.example.ProjetoFinalArqObj.Notificacao;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record NotificacaoResponseDTO(
        Integer id,
        Integer userId,
        Integer habitoId,
        String habitoTitulo,
        boolean visto,
        LocalDate dataReferencia,
        boolean ativo,
        LocalDateTime dataCriacao
) {
    public static NotificacaoResponseDTO of(Notificacao notificacao) {
        return new NotificacaoResponseDTO(
                notificacao.getId(),
                notificacao.getUser().getId(),
                notificacao.getHabito().getId(),
                notificacao.getHabito().getTitulo(),
                notificacao.isVisto(),
                notificacao.getDataReferencia(),
                notificacao.isAtivo(),
                notificacao.getDataCriacao()
        );
    }
}
