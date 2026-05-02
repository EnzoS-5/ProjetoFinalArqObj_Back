package com.example.ProjetoFinalArqObj.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/usuarios"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers(){
        return userService.listarUsuarios();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id){
        return userService.buscaPorId(id);
    }

    @PostMapping
    public User saveUser(@RequestHeader String nome, @RequestHeader String email, @RequestHeader String senha){
        return userService.criaUsuario(nome, email, senha);
    }

    @PutMapping("/{id}")
    public User editUser(@PathVariable Integer id, @RequestHeader String nome, @RequestHeader String senha){
        return userService.edit(id, nome, senha);
    }

    @DeleteMapping("/{id}")
    public List<User> deleteUser(@PathVariable Integer id){
        return userService.delete(id);
    }
}
