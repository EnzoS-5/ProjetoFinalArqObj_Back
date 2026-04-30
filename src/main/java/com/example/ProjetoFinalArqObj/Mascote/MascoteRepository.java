package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MascoteRepository extends JpaRepository<Mascote, Integer> {
    Optional<Mascote> findByUser(User user);
}
