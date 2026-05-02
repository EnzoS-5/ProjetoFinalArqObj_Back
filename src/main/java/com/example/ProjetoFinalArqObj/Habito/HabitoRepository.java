package com.example.ProjetoFinalArqObj.Habito;

import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HabitoRepository extends JpaRepository<Habito, Integer> {
    Optional<Habito> findByIdAndAtivoTrue(Integer id);
    List<Habito> findAllByUserAndAtivoTrue(User user);
    List<Habito> findAllByAtivoTrue();
    boolean existsByUserAndAtivoTrueAndRegistroDiarioTrue(User user);
}

