package com.example.ProjetoFinalArqObj.Plano;

import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanoRepository extends JpaRepository<Plano, Integer> {
    List<Plano> findAllByUserAndAtivoTrue(User user);

    Optional<Plano> findByIdAndAtivoTrue(Integer id);
}
