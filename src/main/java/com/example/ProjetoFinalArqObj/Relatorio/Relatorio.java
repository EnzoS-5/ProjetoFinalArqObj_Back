package com.example.ProjetoFinalArqObj.Relatorio;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.Meta.Meta;
import com.example.ProjetoFinalArqObj.Plano.Plano;
import com.example.ProjetoFinalArqObj.User.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Relatorio {
    private User user;
    private List<Habito> habitos = new ArrayList<>();
    private List<Meta> metas = new ArrayList<>();
    private List<Plano> planos = new ArrayList<>();
    private LocalDateTime dataGeracao;
}
