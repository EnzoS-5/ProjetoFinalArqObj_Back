package com.example.ProjetoFinalArqObj.Mascote;

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
@RequestMapping({"/mascote", "/mascote/"})
public class MascoteController {
    @Autowired
    private MascoteService mascoteService;


    @GetMapping("/{id}")
    public MascoteResponseDTO findById(@PathVariable Integer id) {
        return MascoteResponseDTO.of(mascoteService.buscarPorId(id));
    }


    @GetMapping
    public List<MascoteResponseDTO> listarUsuarioLogado() {
        return mascoteService.listarDoUsuarioLogado().stream().map(MascoteResponseDTO::of).toList();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MascoteResponseDTO criar(@RequestBody CriarMascoteRequestDTO request) {
        Mascote mascote = mascoteService.criar(request.hp(), request.check());
        return MascoteResponseDTO.of(mascote);
    }


    @PatchMapping("/{id}/check")
    public MascoteResponseDTO atualizarCheck(@PathVariable Integer id, @RequestBody AtualizarCheckRequestDTO request) {
        return MascoteResponseDTO.of(mascoteService.atualizarCheck(id, request.check()));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        mascoteService.deletarLogicamente(id);
    }
}
