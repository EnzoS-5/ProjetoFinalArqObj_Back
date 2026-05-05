package com.example.ProjetoFinalArqObj.Plano;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/plano")
public class PlanoController {

    @Autowired
    private PlanoService planoService;

    @GetMapping("/{id}")
    public PlanoResponseDTO findById(@PathVariable Integer id) {
        return PlanoResponseDTO.of(planoService.buscarPorId(id));
    }

    @GetMapping
    public List<PlanoResponseDTO> listarUsuarioLogado() {
        return planoService.listarDoUsuarioLogado().stream().map(PlanoResponseDTO::of).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlanoResponseDTO criar(@RequestBody CriarPlanoRequestDTO request) {
        return PlanoResponseDTO.of(planoService.criar(request.titulo(), request.descricao(), request.habitoIds(), request.metaId()));
    }

    @PutMapping("/{id}")
    public PlanoResponseDTO atualizar(@PathVariable Integer id, @RequestBody AtualizarPlanoRequestDTO request) {
        return PlanoResponseDTO.of(planoService.atualizar(id, request.titulo(), request.descricao(), request.habitoIds(), request.metaId()));
    }

    @PatchMapping("/{id}/concluido")
    public PlanoResponseDTO concluirPlano(@PathVariable Integer id) {
        return PlanoResponseDTO.of(planoService.marcarComoConcluido(id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        planoService.deletarLogicamente(id);
    }
}
