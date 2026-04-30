package com.example.ProjetoFinalArqObj.Mascote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mascote")
public class MascoteController {

    @Autowired
    private MascoteService mascoteService;


    @GetMapping("/{id}")
    public Mascote findById(@PathVariable Integer id) {
        //find user by id find mascote by user
        return null;
    }

    /*@PostMapping
    public Mascote saveDisciplina(@RequestBody SaveProfessorDTO professor) {
        return professorService.save(professor);
    }*/
//TODO dtos


}
