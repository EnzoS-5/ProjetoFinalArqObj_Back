package com.example.ProjetoFinalArqObj.Ranking;

import com.example.ProjetoFinalArqObj.Ranking.dto.RankingUsuarioDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Ranking {
    private List<RankingUsuarioDTO> usuarios = new ArrayList<>();
}
