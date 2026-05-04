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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
            throw new ResponseStatusException(UNAUTHORIZED, "Usuario nao autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario logado nao encontrado."));
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
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano nao encontrado."));
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
            throw new ResponseStatusException(BAD_REQUEST, "Titulo e obrigatorio.");
        }

        Plano plano = new Plano();
        plano.setUser(usuarioLogado);
        plano.setTitulo(titulo.trim());
        plano.setDescricao(descricao == null ? null : descricao.trim());
        plano.setConcluido(false);
        plano.setAtivo(true);
        plano.setHabitos(resolverHabitosDoPlano(habitoIds, usuarioLogado));

        return planoRepository.save(plano);
    }

    @Transactional
    public Plano atualizar(Integer id, String titulo, String descricao, List<Integer> habitoIds) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano nao encontrado."));
        validarDono(plano, usuarioLogado);

        if (titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Titulo e obrigatorio.");
        }

        plano.setTitulo(titulo.trim());
        plano.setDescricao(descricao == null ? null : descricao.trim());
        plano.setHabitos(resolverHabitosDoPlano(habitoIds, usuarioLogado));

        return planoRepository.save(plano);
    }

    @Transactional
    public Plano marcarComoConcluido(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano nao encontrado."));
        validarDono(plano, usuarioLogado);

        if (!plano.isConcluido()) {
            plano.setConcluido(true);
        }

        return planoRepository.save(plano);
    }

    @Transactional
    public void deletarLogicamente(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Plano plano = planoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Plano nao encontrado."));
        validarDono(plano, usuarioLogado);
        plano.setAtivo(false);
        planoRepository.save(plano);
    }

    private List<Habito> resolverHabitosDoPlano(List<Integer> habitoIds, User usuarioLogado) {
        if (habitoIds == null || habitoIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<Habito> habitos = habitoRepository.findAllById(habitoIds);
        Set<Integer> idsEncontrados = new HashSet<>(habitos.stream().map(Habito::getId).toList());

        for (Integer habitoId : habitoIds) {
            if (!idsEncontrados.contains(habitoId)) {
                throw new ResponseStatusException(NOT_FOUND, "Habito nao encontrado.");
            }
        }

        for (Habito habito : habitos) {
            if (!habito.isAtivo()) {
                throw new ResponseStatusException(BAD_REQUEST, "Nao e possivel associar habito inativo ao plano.");
            }
            if (!habito.getUser().getId().equals(usuarioLogado.getId())) {
                throw new ResponseStatusException(FORBIDDEN, "Sem acesso a um ou mais habitos informados.");
            }
        }

        return habitos;
    }
}
