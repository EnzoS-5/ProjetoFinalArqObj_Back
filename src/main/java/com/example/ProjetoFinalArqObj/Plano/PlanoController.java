package com.example.ProjetoFinalArqObj.Plano;


import com.example.ProjetoFinalArqObj.Habito.HabitoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/plano")
public class PlanoController {


    @Autowired
    private PlanoService planoService;



/*
    @GetMapping("/{id}")
    public HabitoResponseDTO findById(@PathVariable Integer id){
        return HabitoResponseDTO.of(planoService.buscarPorId(id));
    }
*/}
