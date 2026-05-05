package com.example.ProjetoFinalArqObj.Ranking;

import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RankingRepository {
    private static final String ADMIN_EMAIL = "admin";

    @Autowired
    private UserRepository userRepository;

    public List<User> listarUsuariosOrdenados() {
        return userRepository.findAllByAtivoTrueAndEmailNot(
                ADMIN_EMAIL,
                Sort.by(
                        Sort.Order.desc("xp"),
                        Sort.Order.desc("streak"),
                        Sort.Order.asc("id")
                )
        );
    }
}
