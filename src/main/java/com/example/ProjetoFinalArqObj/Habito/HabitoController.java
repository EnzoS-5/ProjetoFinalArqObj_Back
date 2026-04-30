package com.example.ProjetoFinalArqObj.Habito;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/habito")
public class HabitoController {
    @Autowired
    private HabitoService habitoService;


    @GetMapping("/{id}")
    public HabitoResponse findById(@PathVariable Integer id){
        return HabitoResponse.of(habitoService.buscarPorId(id));
    }


    @GetMapping
    public List<HabitoResponse> listarUsuarioLogado() {
        return habitoService.listarDoUsuarioLogado().stream().map(HabitoResponse::of).toList();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitoResponse criar(@RequestBody CriarHabitoRequest request) {
        Habito habito = habitoService.criar(request.titulo(), request.descricao());
        return HabitoResponse.of(habito);
    }


    @PatchMapping("/{id}/registro-diario")
    public HabitoResponse concluirRegistroDiario(@PathVariable Integer id) {
        return HabitoResponse.of(habitoService.marcarRegistroDiarioComoConcluido(id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        habitoService.deletarLogicamente(id);
    }


    public record CriarHabitoRequest(String titulo, String descricao) {
    }


    public record HabitoResponse(
            Integer id,
            Integer userId,
            String titulo,
            String descricao,
            boolean registroDiario,
            int streakInterno,
            boolean ativo
    ) {
        public static HabitoResponse of(Habito habito) {
            return new HabitoResponse(
                    habito.getId(),
                    habito.getUser().getId(),
                    habito.getTitulo(),
                    habito.getDescricao(),
                    habito.isRegistroDiario(),
                    habito.getStreakInterno(),
                    habito.isAtivo()
            );
        }
    }
}
