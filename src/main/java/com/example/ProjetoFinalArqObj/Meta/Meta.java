package com.example.ProjetoFinalArqObj.Meta;

import com.example.ProjetoFinalArqObj.User.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Meta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Column(nullable = false)
    private String titulo;


    @Column(nullable = true)
    private String descricao;


    @Column(nullable = true)
    private LocalDate dataLimite;


    @Column(nullable = false)
    private boolean concluido;


    @Column(nullable = false)
    private boolean ativo;
}
