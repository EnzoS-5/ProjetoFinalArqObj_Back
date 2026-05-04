package com.example.ProjetoFinalArqObj.Plano;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
public class Plano {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private User user;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "plano_habitos",
            joinColumns = @JoinColumn(name = "plano_id"),
            inverseJoinColumns = @JoinColumn(name = "habito_id")
    )
    private List<Habito> habitos = new ArrayList<>();

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false)
    private boolean concluido;

    @Column(nullable = false)
    private boolean ativo;

    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;
}
