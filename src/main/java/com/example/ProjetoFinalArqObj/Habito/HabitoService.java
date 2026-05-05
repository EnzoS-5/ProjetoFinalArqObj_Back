package com.example.ProjetoFinalArqObj.Habito;

import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class HabitoService {
    @Autowired
    private HabitoRepository habitoRepository;


    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public Habito buscarPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Habito habito = habitoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Hábito não encontrado."));
        validarDono(habito, usuarioLogado);
        return habito;
    }


    @Transactional(readOnly = true)
    public List<Habito> listarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return habitoRepository.findAllByUserAndAtivoTrue(usuarioLogado);
    }


    @Transactional
    public Habito criar(String titulo, String descricao) {
        if (titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Título é obrigatório.");
        }
        User usuarioLogado = buscarUsuarioLogado();

        List<Habito> habitosUsuario = habitoRepository.findAllByUserAndAtivoTrue(usuarioLogado);
        if (habitosUsuario.size() < usuarioLogado.getMaxHabitos()) {
            Habito habito = new Habito();
            habito.setUser(usuarioLogado);
            habito.setTitulo(titulo.trim());
            habito.setDescricao(descricao == null ? null : descricao.trim());
            habito.setRegistroDiario(false);
            habito.setStreakInterno(0);
            habito.setAtivo(true);
            return habitoRepository.save(habito);
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "Suba de nível para liberar mais hábitos!");
        }
    }


    @Transactional
    public Habito marcarRegistroDiarioComoConcluido(Integer habitoId) {
        User usuarioLogado = buscarUsuarioLogado();
        Habito habito = habitoRepository.findByIdAndAtivoTrue(habitoId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Hábito não encontrado."));
        validarDono(habito, usuarioLogado);
        if (!habito.isRegistroDiario()) {
            habito.setRegistroDiario(true);
            habito.setStreakInterno(habito.getStreakInterno() + 1);
            if (usuarioLogado.podeAumentarStreakHoje()) {
                usuarioLogado.incrementarStreakComData();
            }
            usuarioLogado.adicionarXp(10);
        }
        userRepository.save(usuarioLogado);
        return habitoRepository.save(habito);
    }


    @Transactional
    public void deletarLogicamente(Integer habitoId) {
        User usuarioLogado = buscarUsuarioLogado();
        Habito habito = habitoRepository.findByIdAndAtivoTrue(habitoId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Hábito não encontrado."));
        validarDono(habito, usuarioLogado);
        habito.setAtivo(false);
        habitoRepository.save(habito);
    }

    @Transactional
    public void resetarStreakSeRegistroDiarioFalso() {
        List<Habito> habitosAtivos = habitoRepository.findAllByAtivoTrue();
        for (Habito habito : habitosAtivos) {
            if (!habito.isRegistroDiario()) {
                habito.setStreakInterno(0);
                habitoRepository.save(habito);
            }
        }
    }


    @Transactional
    @Scheduled(cron = "0 0 6 * * *", zone = "America/Sao_Paulo")
    public void resetDiario() {
        List<Habito> habitosAtivos = habitoRepository.findAllByAtivoTrue();
        for (Habito habito : habitosAtivos) {
            User usuario = habito.getUser();
            if (!habito.isRegistroDiario()) {
                habito.setStreakInterno(0);
                usuario.resetarStreak();
            }
            habito.setRegistroDiario(false);
            userRepository.save(usuario);
            habitoRepository.save(habito);
        }
    }


    private User buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Usuário não autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuário logado não encontrado."));
    }


    private void validarDono(Habito habito, User usuarioLogado) {
        if (!habito.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este hábito.");
        }
    }
}
