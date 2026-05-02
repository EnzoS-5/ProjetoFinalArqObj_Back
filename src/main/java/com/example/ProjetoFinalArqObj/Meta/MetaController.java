package com.example.ProjetoFinalArqObj.Meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meta")
public class MetaController {
    @Autowired
    private MetaService metaService;


    @GetMapping("/{id}")
    public MetaResponseDTO findById(@PathVariable Integer id) {
        return MetaResponseDTO.of(metaService.buscarPorId(id));
    }


    @GetMapping
    public List<MetaResponseDTO> listarUsuarioLogado() {
        return metaService.listarDoUsuarioLogado().stream().map(MetaResponseDTO::of).toList();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetaResponseDTO criar(@RequestBody CriarMetaRequestDTO request) {
        Meta meta = metaService.criar(request.titulo(), request.descricao(), request.dataLimite());
        return MetaResponseDTO.of(meta);
    }


    @PatchMapping("/{id}/concluido")
    public MetaResponseDTO concluirMeta(@PathVariable Integer id) {
        return MetaResponseDTO.of(metaService.marcarComoConcluido(id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        metaService.deletarLogicamente(id);
    }
}
