package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Getter
@Setter
@Entity
public class Mascote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private User user;


    @Column(nullable = false)
    private int hp;


    @Column(nullable = false)
    private boolean check;


    @UpdateTimestamp
    private LocalDateTime dataAtualizacao;


    public static @NonNull Mascote toModel(int user_id) {
        Mascote mascote = new Mascote();
        mascote.setHp(200);
        mascote.setUser(new User("","","",0));//User.findUser(user_id)); ainda nao feito
        mascote.setCheck(true);
        return mascote;

    }
}
