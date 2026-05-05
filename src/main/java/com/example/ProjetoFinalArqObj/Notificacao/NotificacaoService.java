package com.example.ProjetoFinalArqObj.Notificacao;

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

import java.time.LocalDate;
import java.util.List;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class NotificacaoService {
    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public Notificacao buscarPorId(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Notificacao notificacao = notificacaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Notificacao nao encontrada."));
        validarDono(notificacao, usuarioLogado);
        return notificacao;
    }

    @Transactional(readOnly = true)
    public List<Notificacao> listarDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return notificacaoRepository.findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(usuarioLogado);
    }

    @Transactional(readOnly = true)
    public List<Notificacao> listarNaoVistasDoUsuarioLogado() {
        User usuarioLogado = buscarUsuarioLogado();
        return notificacaoRepository.findAllByUserAndAtivoTrueAndVistoFalseOrderByDataReferenciaDescDataCriacaoDesc(usuarioLogado);
    }

    @Transactional
    public Notificacao criar(Integer habitoId, LocalDate dataReferencia) {
        if (habitoId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Habito e obrigatorio.");
        }

        User usuarioLogado = buscarUsuarioLogado();
        Habito habito = habitoRepository.findByIdAndAtivoTrue(habitoId)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Habito nao encontrado."));
        validarDono(habito, usuarioLogado);

        LocalDate data = dataReferencia == null ? LocalDate.now() : dataReferencia;
        return criarInterno(usuarioLogado, habito, data);
    }

    @Transactional
    public Notificacao criarNotificacaoStreakReset(User usuario, Habito habito, LocalDate dataReferencia) {
        if (usuario == null || habito == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Usuario e habito sao obrigatorios.");
        }

        return criarInterno(usuario, habito, dataReferencia == null ? LocalDate.now() : dataReferencia);
    }

    @Transactional
    public Notificacao marcarComoVista(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Notificacao notificacao = notificacaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Notificacao nao encontrada."));
        validarDono(notificacao, usuarioLogado);
        if (!notificacao.isVisto()) {
            notificacao.setVisto(true);
        }
        return notificacaoRepository.save(notificacao);
    }

    @Transactional
    public void deletarLogicamente(Integer id) {
        User usuarioLogado = buscarUsuarioLogado();
        Notificacao notificacao = notificacaoRepository.findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Notificacao nao encontrada."));
        validarDono(notificacao, usuarioLogado);
        notificacao.setAtivo(false);
        notificacaoRepository.save(notificacao);
    }

    private Notificacao criarInterno(User usuario, Habito habito, LocalDate dataReferencia) {
        if (notificacaoRepository.existsByUserAndHabitoAndDataReferenciaAndAtivoTrue(usuario, habito, dataReferencia)) {
            return notificacaoRepository.findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(usuario)
                    .stream()
                    .filter(notificacao -> notificacao.getHabito().getId().equals(habito.getId()))
                    .filter(notificacao -> dataReferencia.equals(notificacao.getDataReferencia()))
                    .findFirst()
                    .orElseGet(() -> salvarNovaNotificacao(usuario, habito, dataReferencia));
        }

        return salvarNovaNotificacao(usuario, habito, dataReferencia);
    }

    private Notificacao salvarNovaNotificacao(User usuario, Habito habito, LocalDate dataReferencia) {
        Notificacao notificacao = new Notificacao();
        notificacao.setUser(usuario);
        notificacao.setHabito(habito);
        notificacao.setVisto(false);
        notificacao.setDataReferencia(dataReferencia);
        notificacao.setAtivo(true);
        return notificacaoRepository.save(notificacao);
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

    private void validarDono(Notificacao notificacao, User usuarioLogado) {
        if (!notificacao.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a esta notificacao.");
        }
    }

    private void validarDono(Habito habito, User usuarioLogado) {
        if (!habito.getUser().getId().equals(usuarioLogado.getId())) {
            throw new ResponseStatusException(FORBIDDEN, "Sem acesso a este habito.");
        }
    }
}
