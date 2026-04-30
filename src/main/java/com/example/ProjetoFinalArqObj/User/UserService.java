package com.example.ProjetoFinalArqObj.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User buscaPorId(Integer id){
        return userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrada."));
    }

    public List<User> listarUsuarios(){
        return userRepository.findAll();
    }

    public User criaUsuario(String nome, String email, String senha){
        if (nome == null || nome.isBlank()){
            throw new ResponseStatusException(BAD_REQUEST, "Nome é obrigatório.");
        }
        if (email == null || email.isBlank()){
            throw new ResponseStatusException(BAD_REQUEST, "E-mail é obrigatório.");
        }
        if (senha == null || senha.isBlank()){
            throw new ResponseStatusException(BAD_REQUEST, "Senha é obrigatório.");
        }

        User user = new User(nome, email, senha);
        userRepository.save(user);
        return user;
    }

    public User edit(Integer id, String nome, String senha){
        User user = userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrada."));

        user.setNome(nome);
        user.setSenha(senha);

        userRepository.save(user);

        return user;
    }

    public List<User> delete(Integer id){
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrada.")));
        return userRepository.findAll();
    }
}
