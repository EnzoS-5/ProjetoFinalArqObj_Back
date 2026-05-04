package com.example.ProjetoFinalArqObj.User;

public record CriarUserRequestDTO(
        String nome,
        String email,
        String senha
) {
}
