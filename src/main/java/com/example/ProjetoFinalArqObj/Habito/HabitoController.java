package com.example.ProjetoFinalArqObj.Habito;

import com.example.ProjetoFinalArqObj.Habito.dto.HabitoResponseDTO;
import com.example.ProjetoFinalArqObj.Habito.dto.CriarHabitoRequestDTO;
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
    public HabitoResponseDTO findById(@PathVariable Integer id){
        return HabitoResponseDTO.of(habitoService.buscarPorId(id));
    }


    @GetMapping
    public List<HabitoResponseDTO> listarUsuarioLogado() {
        return habitoService.listarDoUsuarioLogado().stream().map(HabitoResponseDTO::of).toList();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitoResponseDTO criar(@RequestBody CriarHabitoRequestDTO request) {
        Habito habito = habitoService.criar(request.titulo(), request.descricao());
        return HabitoResponseDTO.of(habito);
    }


    @PatchMapping("/{id}/registro-diario")
    public HabitoResponseDTO concluirRegistroDiario(@PathVariable Integer id) {
        return HabitoResponseDTO.of(habitoService.marcarRegistroDiarioComoConcluido(id));
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        habitoService.deletarLogicamente(id);
    }
}
