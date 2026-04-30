package com.example.ProjetoFinalArqObj.Meta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/meta")
public class MetaController {
    @Autowired
    private MetaService metaService;


    @GetMapping("/{id}")
    public MetaResponse findById(@PathVariable Integer id) {
        return MetaResponse.of(metaService.buscarPorId(id));
    }


    @GetMapping
    public List<MetaResponse> listarUsuarioLogado() {
        return metaService.listarDoUsuarioLogado().stream().map(MetaResponse::of).toList();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MetaResponse criar(@RequestBody CriarMetaRequest request) {
        Meta meta = metaService.criar(request.titulo(), request.descricao(), request.dataLimite());
        return MetaResponse.of(meta);
    }


    @PatchMapping("/{id}/concluido")
    public MetaResponse concluirMeta(@PathVariable Integer id) {
        return MetaResponse.of(metaService.marcarComoConcluido(id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        metaService.deletarLogicamente(id);
    }


    public record CriarMetaRequest(String titulo, String descricao, LocalDate dataLimite) {
    }


    public record MetaResponse(
            Integer id,
            Integer userId,
            String titulo,
            String descricao,
            LocalDate dataLimite,
            boolean concluido,
            boolean ativo
    ) {
        public static MetaResponse of(Meta meta) {
            return new MetaResponse(
                    meta.getId(),
                    meta.getUser().getId(),
                    meta.getTitulo(),
                    meta.getDescricao(),
                    meta.getDataLimite(),
                    meta.isConcluido(),
                    meta.isAtivo()
            );
        }
    }
}
