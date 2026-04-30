package com.example.ProjetoFinalArqObj.Habito;

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
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Habito {
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


    @Column(nullable = false)
    private boolean registroDiario;


    @Column(nullable = false)
    private int streakInterno;


    @Column(nullable = false)
    private boolean ativo;


    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}
