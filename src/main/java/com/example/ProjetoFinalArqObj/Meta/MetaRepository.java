package com.example.ProjetoFinalArqObj.Meta;

import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MetaRepository extends JpaRepository<Meta, Integer> {
    Optional<Meta> findByIdAndAtivoTrue(Integer id);
    List<Meta> findAllByUserAndAtivoTrue(User user);
}
