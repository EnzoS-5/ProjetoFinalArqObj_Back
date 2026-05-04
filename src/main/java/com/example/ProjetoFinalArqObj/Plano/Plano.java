package com.example.ProjetoFinalArqObj.Plano;


import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "Planos", nullable = false)
    private List<Habito> habito;



    @Column(nullable = false)
    private String titulo;


    @Column(nullable = true)
    private String descricao;


    @Column(nullable = false)
    private boolean registroDiario;


    @Column(nullable = false)
    private int streakInterno;


    @Column(nullable = false)
    private boolean ativo;


    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;


}
