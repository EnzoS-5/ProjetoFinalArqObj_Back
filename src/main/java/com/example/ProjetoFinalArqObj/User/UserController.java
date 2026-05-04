package com.example.ProjetoFinalArqObj.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/usuarios"})
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponseDTO> getUsers(){
        return userService.listarUsuarios().stream().map(UserResponseDTO::of).toList();
    }

    @GetMapping("/{id}")
    public UserResponseDTO getUser(@PathVariable Integer id){
        return UserResponseDTO.of(userService.buscaPorId(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO saveUser(@RequestBody UserRequestDTO request){
        return UserResponseDTO.of(userService.criaUsuario(request.nome(), request.email(), request.senha()));
    }

    @PutMapping("/{id}")
    public UserResponseDTO editUser(@PathVariable Integer id, @RequestBody UserRequestDTO request){
        return UserResponseDTO.of(userService.edit(id, request.nome(), request.senha()));
    }

    @DeleteMapping("/{id}")
    public List<UserResponseDTO> deleteUser(@PathVariable Integer id){
        return userService.delete(id).stream().map(UserResponseDTO::of).toList();
    }
}
