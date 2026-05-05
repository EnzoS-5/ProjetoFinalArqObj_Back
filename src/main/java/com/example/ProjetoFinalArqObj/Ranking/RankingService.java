package com.example.ProjetoFinalArqObj.Ranking;

import com.example.ProjetoFinalArqObj.Ranking.dto.RankingResponseDTO;
import com.example.ProjetoFinalArqObj.Ranking.dto.RankingUsuarioDTO;
import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class RankingService {

    @Autowired
    private RankingRepository rankingRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public RankingResponseDTO listarRankingCompleto() {
        return RankingResponseDTO.of(montarRanking(rankingRepository.listarUsuariosOrdenados()));
    }

    @Transactional(readOnly = true)
    public RankingResponseDTO listarTop(Integer limite) {
        if (limite == null || limite <= 0) {
            throw new ResponseStatusException(BAD_REQUEST, "Limite deve ser maior que zero.");
        }

        List<User> usuariosOrdenados = rankingRepository.listarUsuariosOrdenados();
        int fim = Math.min(limite, usuariosOrdenados.size());
        return RankingResponseDTO.of(montarRanking(usuariosOrdenados.subList(0, fim)));
    }

    @Transactional(readOnly = true)
    public RankingUsuarioDTO buscarMinhaPosicao() {
        User usuarioLogado = buscarUsuarioLogado();
        List<User> usuariosOrdenados = rankingRepository.listarUsuariosOrdenados();

        for (int i = 0; i < usuariosOrdenados.size(); i++) {
            User user = usuariosOrdenados.get(i);
            if (user.getId().equals(usuarioLogado.getId())) {
                return RankingUsuarioDTO.of(user, i + 1);
            }
        }

        throw new ResponseStatusException(UNAUTHORIZED, "Usuario logado nao encontrado no ranking.");
    }

    private List<RankingUsuarioDTO> montarRanking(List<User> usuarios) {
        List<RankingUsuarioDTO> usuariosRanking = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {
            usuariosRanking.add(RankingUsuarioDTO.of(usuarios.get(i), i + 1));
        }

        return usuariosRanking;
    }

    private User buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Usuario nao autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario logado nao encontrado."));
    }
}
