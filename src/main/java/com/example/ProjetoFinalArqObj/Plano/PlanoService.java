package com.example.ProjetoFinalArqObj.Plano;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.Habito.HabitoRepository;
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

import static org.springframework.http.HttpStatus.*;

@Service
public class PlanoService {

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitoRepository habitoRepository;

    private User buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Usuário não autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuário logado não encontrado."));
    }

    private void validarDono(Plano plano, User usuarioLogado) {
        if (!plano.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este plano.");
        }
    }

    @Transactional(readOnly = true)
    public Plano buscarPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano não encontrado."));
        validarDono(plano, usuarioLogado);
        return plano;
    }

    @Transactional(readOnly = true)
    public List<Plano> listarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return planoRepository.findAllByUserAndAtivoTrue(usuarioLogado);
    }

    @Transactional
    public Plano criar(String titulo, String descricao, List<Integer> habitoIds) {
        User usuarioLogado = buscarUsuarioLogado();

        if (titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Título é obrigatório.");
        }

        Plano plano = new Plano();
        plano.setUser(usuarioLogado);
        plano.setTitulo(titulo);
        plano.setDescricao(descricao);
        plano.setConcluido(false);
        plano.setAtivo(true);

        List<Habito> habitos = new ArrayList<>();
        if (habitoIds != null && !habitoIds.isEmpty()) {
            habitos.addAll(habitoRepository.findAllById(habitoIds));
        }
        plano.setHabitos(habitos);

        return planoRepository.save(plano);
    }

    @Transactional
    public Plano marcarComoConcluido(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano não encontrado."));
        validarDono(plano, usuarioLogado);
        plano.setConcluido(true);
        return planoRepository.save(plano);
    }

    @Transactional
    public void deletarLogicamente(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano não encontrado."));
        validarDono(plano, usuarioLogado);
        plano.setAtivo(false);
        planoRepository.save(plano);
    }
}
