package com.example.ProjetoFinalArqObj.Meta.dto;

import java.time.LocalDate;

public record CriarMetaRequestDTO(String titulo, String descricao, LocalDate dataLimite) {
}
