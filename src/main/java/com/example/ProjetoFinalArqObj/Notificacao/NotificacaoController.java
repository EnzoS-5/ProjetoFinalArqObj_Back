package com.example.ProjetoFinalArqObj.Notificacao;

import com.example.ProjetoFinalArqObj.Notificacao.dto.NotificacaoResponseDTO;
import com.example.ProjetoFinalArqObj.Notificacao.dto.CriarNotificacaoRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notificacao")
public class NotificacaoController {
    @Autowired
    private NotificacaoService notificacaoService;

    @GetMapping("/{id}")
    public NotificacaoResponseDTO findById(@PathVariable Integer id) {
        return NotificacaoResponseDTO.of(notificacaoService.buscarPorId(id));
    }

    @GetMapping
    public List<NotificacaoResponseDTO> listarUsuarioLogado() {
        return notificacaoService.listarDoUsuarioLogado().stream().map(NotificacaoResponseDTO::of).toList();
    }

    @GetMapping("/nao-vistas")
    public List<NotificacaoResponseDTO> listarNaoVistasUsuarioLogado() {
        return notificacaoService.listarNaoVistasDoUsuarioLogado().stream().map(NotificacaoResponseDTO::of).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NotificacaoResponseDTO criar(@RequestBody CriarNotificacaoRequestDTO request) {
        Notificacao notificacao = notificacaoService.criar(request.habitoId(), request.dataReferencia());
        return NotificacaoResponseDTO.of(notificacao);
    }

    @PatchMapping("/{id}/visto")
    public NotificacaoResponseDTO marcarComoVista(@PathVariable Integer id) {
        return NotificacaoResponseDTO.of(notificacaoService.marcarComoVista(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        notificacaoService.deletarLogicamente(id);
    }
}
