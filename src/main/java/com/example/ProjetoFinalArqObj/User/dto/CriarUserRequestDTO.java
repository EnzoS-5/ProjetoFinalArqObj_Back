package com.example.ProjetoFinalArqObj.User.dto;

public record CriarUserRequestDTO(
        String nome,
        String email,
        String senha
) {
}
