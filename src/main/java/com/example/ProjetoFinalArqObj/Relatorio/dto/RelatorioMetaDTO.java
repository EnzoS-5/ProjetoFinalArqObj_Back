package com.example.ProjetoFinalArqObj.Relatorio.dto;

import com.example.ProjetoFinalArqObj.Meta.Meta;

import java.time.LocalDate;

public record RelatorioMetaDTO(
        Integer id,
        String titulo,
        String descricao,
        LocalDate dataLimite,
        boolean cumprida
) {
    public static RelatorioMetaDTO of(Meta meta) {
        return new RelatorioMetaDTO(
                meta.getId(),
                meta.getTitulo(),
                meta.getDescricao(),
                meta.getDataLimite(),
                meta.isConcluido()
        );
    }
}
