package com.example.ProjetoFinalArqObj.Meta;

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
public class MetaService {
    @Autowired
    private MetaRepository metaRepository;


    @Autowired
    private UserRepository userRepository;


    @Transactional(readOnly = true)
    public Meta buscarPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Meta meta = metaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Meta não encontrada."));
        validarDono(meta, usuarioLogado);
        return meta;
    }


    @Transactional(readOnly = true)
    public List<Meta> listarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return metaRepository.findAllByUserAndAtivoTrue(usuarioLogado);
    }


    @Transactional
    public Meta criar(String titulo, String descricao, LocalDate dataLimite) {
        if (titulo == null || titulo.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Título é obrigatório.");
        }
        User usuarioLogado = buscarUsuarioLogado();

        Meta meta = new Meta();
        meta.setUser(usuarioLogado);
        meta.setTitulo(titulo.trim());
        meta.setDescricao(descricao == null ? null : descricao.trim());
        meta.setDataLimite(dataLimite);
        meta.setConcluido(false);
        meta.setAtivo(true);
        return metaRepository.save(meta);
    }


    @Transactional
    public Meta marcarComoConcluido(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Meta meta = metaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Meta não encontrada."));
        validarDono(meta, usuarioLogado);
        if (!meta.isConcluido()) {
            meta.setConcluido(true);
            usuarioLogado.adicionarXp(50);
            userRepository.save(usuarioLogado);
        }
        return metaRepository.save(meta);
    }


    @Transactional
    public void deletarLogicamente(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Meta meta = metaRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Meta não encontrada."));
        validarDono(meta, usuarioLogado);
        meta.setAtivo(false);
        metaRepository.save(meta);
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


    private void validarDono(Meta meta, User usuarioLogado) {
        if (!meta.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a esta meta.");
        }
    }
}
