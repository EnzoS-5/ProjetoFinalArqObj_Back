package com.example.ProjetoFinalArqObj.User;

import com.example.ProjetoFinalArqObj.Mascote.Mascote;
import com.example.ProjetoFinalArqObj.Mascote.MascoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MascoteRepository mascoteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User buscaPorId(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario nao encontrado."));
    }

    public List<User> listarUsuarios() {
        return userRepository.findAll();
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

        User user = new User(nome.trim(), email.trim(), passwordEncoder.encode(senha));
        userRepository.save(user);

        Mascote mascote = new Mascote();
        mascote.setUser(user);
        mascote.setHp(100);
        mascote.setCheck(false);
        mascote.setAtivo(true);
        mascoteRepository.save(mascote);

        return user;
    }

    @Transactional
    public User edit(Integer id, String nome, String senha) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario nao encontrado."));

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

    public List<User> delete(Integer id) {
        userRepository.delete(userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario nao encontrado.")));
        return userRepository.findAll();
    }
}
