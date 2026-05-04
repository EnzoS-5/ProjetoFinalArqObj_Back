package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
@Table(name = "mascote")
public class Mascote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;


    @Column(nullable = false)
    private int hp;


    @Column(name = "check_status", nullable = false)
    private boolean check;


    @Column(nullable = false)
    private boolean ativo;


    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private int lastVerifiedStreak;
}
