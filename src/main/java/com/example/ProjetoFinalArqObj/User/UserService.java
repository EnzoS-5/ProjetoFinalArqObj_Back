package com.example.ProjetoFinalArqObj.User;

import com.example.ProjetoFinalArqObj.Habito.HabitoRepository;
import com.example.ProjetoFinalArqObj.Mascote.Mascote;
import com.example.ProjetoFinalArqObj.Mascote.MascoteRepository;
import com.example.ProjetoFinalArqObj.Meta.MetaRepository;
import com.example.ProjetoFinalArqObj.Notificacao.NotificacaoRepository;
import com.example.ProjetoFinalArqObj.Plano.PlanoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MascoteRepository mascoteRepository;

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private MetaRepository metaRepository;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User buscaPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        validarMesmoUsuario(id, usuarioLogado);
        return usuarioLogado;
    }

    @Transactional(readOnly = true)
    public List<User> listarUsuarios() {
        return List.of(buscarUsuarioLogado());
    }

    @Transactional
    public User criaUsuario(String nome, String email, String senha) {
        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Nome e obrigatorio.");
        }
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "E-mail e obrigatorio.");
        }
        if (senha == null || senha.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Senha e obrigatoria.");
        }

        String emailTratado = email.trim();
        if (userRepository.findByEmail(emailTratado).isPresent()) {
            throw new ResponseStatusException(CONFLICT, "E-mail ja cadastrado.");
        }

        User user = new User(nome.trim(), emailTratado, passwordEncoder.encode(senha));
        userRepository.save(user);

        Mascote mascote = new Mascote();
        mascote.setUser(user);
        mascote.setHp(100);
        mascote.setCheck(false);
        mascote.setAtivo(true);
        mascote.setLastVerifiedStreak(user.getStreak());
        mascoteRepository.save(mascote);

        return user;
    }

    @Transactional
    public User edit(Integer id, String nome, String senha) {
        User user = buscarUsuarioLogado();
        validarMesmoUsuario(id, user);

        if (nome == null || nome.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Nome e obrigatorio.");
        }
        if (senha == null || senha.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Senha e obrigatoria.");
        }

        user.setNome(nome.trim());
        user.setSenha(passwordEncoder.encode(senha));

        userRepository.save(user);
        return user;
    }

    @Transactional
    public void delete(Integer id) {
        User user = buscarUsuarioLogado();
        validarMesmoUsuario(id, user);

        habitoRepository.findAllByUserAndAtivoTrue(user)
                .forEach(habito -> habito.setAtivo(false));
        metaRepository.findAllByUserAndAtivoTrue(user)
                .forEach(meta -> meta.setAtivo(false));
        planoRepository.findAllByUserAndAtivoTrue(user)
                .forEach(plano -> plano.setAtivo(false));
        mascoteRepository.findAllByUserAndAtivoTrue(user)
                .forEach(mascote -> mascote.setAtivo(false));
        notificacaoRepository.findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(user)
                .forEach(notificacao -> notificacao.setAtivo(false));

        user.setAtivo(false);
        userRepository.save(user);
    }

    private User buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(UNAUTHORIZED, "Usuario nao autenticado.");
        }
        String email = authentication.getName();
        return userRepository.findByEmailAndAtivoTrue(email)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Usuario logado nao encontrado."));
    }

    private void validarMesmoUsuario(Integer id, User usuarioLogado) {
        if (id == null) {
            throw new ResponseStatusException(NOT_FOUND, "Usuario nao encontrado.");
        }
        if (!usuarioLogado.getId().equals(id)) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este usuario.");
        }
    }
}
