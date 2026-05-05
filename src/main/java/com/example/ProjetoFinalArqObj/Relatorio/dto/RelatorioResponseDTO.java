package com.example.ProjetoFinalArqObj.Relatorio.dto;

import com.example.ProjetoFinalArqObj.Relatorio.Relatorio;
import java.time.LocalDateTime;
import java.util.List;

public record RelatorioResponseDTO(
        Integer userId,
        String nomeUsuario,
        int totalHabitos,
        int habitosCumpridos,
        int habitosNaoCumpridos,
        int totalMetas,
        int metasCumpridas,
        int metasNaoCumpridas,
        int totalPlanos,
        int planosCumpridos,
        int planosNaoCumpridos,
        LocalDateTime dataGeracao,
        List<RelatorioHabitoDTO> habitos,
        List<RelatorioMetaDTO> metas,
        List<RelatorioPlanoDTO> planos
) {
    public static RelatorioResponseDTO of(Relatorio relatorio) {
        List<RelatorioHabitoDTO> habitos = relatorio.getHabitos().stream().map(RelatorioHabitoDTO::of).toList();
        List<RelatorioMetaDTO> metas = relatorio.getMetas().stream().map(RelatorioMetaDTO::of).toList();
        List<RelatorioPlanoDTO> planos = relatorio.getPlanos().stream().map(RelatorioPlanoDTO::of).toList();

        int habitosCumpridos = (int) relatorio.getHabitos().stream().filter(habito -> habito.isRegistroDiario()).count();
        int metasCumpridas = (int) relatorio.getMetas().stream().filter(meta -> meta.isConcluido()).count();
        int planosCumpridos = (int) relatorio.getPlanos().stream().filter(plano -> plano.isConcluido()).count();

        return new RelatorioResponseDTO(
                relatorio.getUser().getId(),
                relatorio.getUser().getNome(),
                habitos.size(),
                habitosCumpridos,
                habitos.size() - habitosCumpridos,
                metas.size(),
                metasCumpridas,
                metas.size() - metasCumpridas,
                planos.size(),
                planosCumpridos,
                planos.size() - planosCumpridos,
                relatorio.getDataGeracao(),
                habitos,
                metas,
                planos
        );
    }
}
