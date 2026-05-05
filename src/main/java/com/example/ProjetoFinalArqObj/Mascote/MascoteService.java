package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
public class MascoteService {


    @Autowired
    private MascoteRepository mascoteRepository;


    @Autowired
    private UserRepository userRepository;



    private User buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Usuário não autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuário logado não encontrado."));
    }


    private void validarDono(Mascote mascote, User usuarioLogado) {
        if (!mascote.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este mascote.");
        }
    }

    @Transactional(readOnly = true)
    public Mascote buscarPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Mascote mascote = mascoteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Mascote não encontrado."));
        validarDono(mascote, usuarioLogado);
        return mascote;
    }


    @Transactional(readOnly = true)
    public List<Mascote> listarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return mascoteRepository.findAllByUserAndAtivoTrue(usuarioLogado);
    }


    @Transactional
    public Mascote criar(Integer hp, Boolean check) {
        User usuarioLogado = buscarUsuarioLogado();
        if (hp != null && hp < 0) {
            throw new ResponseStatusException(BAD_REQUEST, "HP não pode ser negativo.");
        }

        Mascote mascoteExistente = mascoteRepository.findByUser(usuarioLogado).orElse(null);
        if (mascoteExistente != null && mascoteExistente.isAtivo()) {
            throw new ResponseStatusException(CONFLICT, "Usuário já possui mascote ativo.");
        }

        Mascote mascote = mascoteExistente == null ? new Mascote() : mascoteExistente;
        mascote.setUser(usuarioLogado);
        mascote.setHp(hp == null ? 100 : hp);
        mascote.setCheck(check != null && check);
        mascote.setAtivo(true);
        mascote.setLastVerifiedStreak(usuarioLogado.getStreak());
        return mascoteRepository.save(mascote);
    }


    @Transactional
    public Mascote atualizarCheck(Integer id, boolean check) {
        User usuarioLogado = buscarUsuarioLogado();
        Mascote mascote = mascoteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Mascote não encontrado."));
        validarDono(mascote, usuarioLogado);
        mascote.setCheck(check);
        return mascoteRepository.save(mascote);
    }


    @Transactional
    public void deletarLogicamente(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Mascote mascote = mascoteRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Mascote não encontrado."));
        validarDono(mascote, usuarioLogado);
        mascote.setAtivo(false);
        mascoteRepository.save(mascote);
    }

    @Transactional
    public void verificarEReduzirHPPorStreakNaoAtualizado(LocalDate dataReferencia) {
        LocalDate dataVerificacao = dataReferencia == null ? LocalDate.now() : dataReferencia;
        List<Mascote> mascotes = mascoteRepository.findAllByAtivoTrue();
        for (Mascote mascote : mascotes) {
            User user = mascote.getUser();
            boolean streakEvoluiuNaData = dataVerificacao.equals(user.getLastStreakDate());
            if (!streakEvoluiuNaData) {
                mascote.setHp(Math.max(0, mascote.getHp() - 10));
            }
            mascote.setLastVerifiedStreak(user.getStreak());
            mascoteRepository.save(mascote);
        }
    }
}
