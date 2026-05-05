package com.example.ProjetoFinalArqObj.Relatorio;

import com.example.ProjetoFinalArqObj.Habito.HabitoRepository;
import com.example.ProjetoFinalArqObj.Meta.MetaRepository;
import com.example.ProjetoFinalArqObj.Plano.PlanoRepository;
import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class RelatorioRepository {

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private PlanoRepository planoRepository;

    public Relatorio gerarPorUsuario(User user) {
        Relatorio relatorio = new Relatorio();
        relatorio.setUser(user);
        relatorio.setHabitos(habitoRepository.findAllByUserAndAtivoTrue(user));
        relatorio.setMetas(metaRepository.findAllByUserAndAtivoTrue(user));
        relatorio.setPlanos(planoRepository.findAllByUserAndAtivoTrue(user));
        relatorio.setDataGeracao(LocalDateTime.now());
        return relatorio;
    }
}
