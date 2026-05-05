package com.example.ProjetoFinalArqObj.Relatorio;

import com.example.ProjetoFinalArqObj.Relatorio.dto.RelatorioResponseDTO;
import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class RelatorioService {

    @Autowired
    private RelatorioRepository relatorioRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public RelatorioResponseDTO gerarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return RelatorioResponseDTO.of(relatorioRepository.gerarPorUsuario(usuarioLogado));
    }

    @Transactional(readOnly = true)
    public RelatorioResponseDTO gerarPorUsuario(Integer userId) {
        User usuarioLogado = buscarUsuarioLogado();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuario nao encontrado."));
        validarAcesso(user, usuarioLogado);
        return RelatorioResponseDTO.of(relatorioRepository.gerarPorUsuario(user));
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

    private void validarAcesso(User user, User usuarioLogado) {
        if (!user.getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este relatorio.");
        }
    }
}
