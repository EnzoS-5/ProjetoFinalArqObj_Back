package com.example.ProjetoFinalArqObj.Notificacao;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Integer> {
    Optional<Notificacao> findByIdAndAtivoTrue(Integer id);
    List<Notificacao> findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(User user);
    List<Notificacao> findAllByUserAndAtivoTrueAndVistoFalseOrderByDataReferenciaDescDataCriacaoDesc(User user);
    boolean existsByUserAndHabitoAndDataReferenciaAndAtivoTrue(User user, Habito habito, LocalDate dataReferencia);
}
