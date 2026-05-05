package com.example.ProjetoFinalArqObj;

import com.example.ProjetoFinalArqObj.Mascote.Mascote;
import com.example.ProjetoFinalArqObj.Mascote.MascoteRepository;
import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class StartupDataInitializer {

    @Bean
    public CommandLineRunner createDefaultAdminUser(
            UserRepository userRepository,
            MascoteRepository mascoteRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            User admin = userRepository.findByEmail("admin").orElseGet(() -> {
                User newAdmin = new User("admin", "admin", passwordEncoder.encode("admin123"));
                return userRepository.save(newAdmin);
            });

            if (mascoteRepository.findByUser(admin).isEmpty()) {
                Mascote mascote = new Mascote();
                mascote.setUser(admin);
                mascote.setHp(100);
                mascote.setCheck(false);
                mascote.setAtivo(true);
                mascoteRepository.save(mascote);
            }
        };
    }
}
