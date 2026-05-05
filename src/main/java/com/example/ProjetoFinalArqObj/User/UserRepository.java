package com.example.ProjetoFinalArqObj.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndAtivoTrue(String email);
    Optional<User> findByIdAndAtivoTrue(Integer id);
    List<User> findAllByAtivoTrue(Sort sort);
    List<User> findAllByAtivoTrueAndEmailNot(String email, Sort sort);
}
