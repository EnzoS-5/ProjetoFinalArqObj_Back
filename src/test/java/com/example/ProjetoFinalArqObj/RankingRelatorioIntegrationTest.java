package com.example.ProjetoFinalArqObj;

import com.example.ProjetoFinalArqObj.User.User;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RankingRelatorioIntegrationTest extends IntegrationTestSupport {

    @Test
    void deveGerarRankingSemAdminERelatorioSomenteComDadosDoUsuario() throws Exception {
        Integer adminId = criarUsuario("Admin", "admin");
        Integer anaId = criarUsuario("Ana", EMAIL_ANA);
        Integer brunoId = criarUsuario("Bruno", EMAIL_BRUNO);
        Integer habitoId = criarHabito(EMAIL_ANA, "Caminhar");
        Integer metaId = criarMeta(EMAIL_ANA, "Completar semana");
        criarPlano(EMAIL_ANA, "Plano semanal", habitoId, metaId);

        User admin = usuario(adminId);
        User ana = usuario(anaId);
        User bruno = usuario(brunoId);
        salvarPontuacao(admin, 999, 99);
        salvarPontuacao(ana, 100, 2);
        salvarPontuacao(bruno, 200, 1);

        mockMvc.perform(get("/ranking")
                        .with(auth(EMAIL_ANA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalUsuarios").value(2))
                .andExpect(jsonPath("$.usuarios[0].userId").value(brunoId))
                .andExpect(jsonPath("$.usuarios[1].userId").value(anaId))
                .andExpect(jsonPath("$.usuarios[?(@.userId == %d)]".formatted(adminId)).isEmpty());

        mockMvc.perform(get("/relatorio")
                        .with(auth(EMAIL_ANA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(anaId))
                .andExpect(jsonPath("$.totalHabitos").value(1))
                .andExpect(jsonPath("$.totalMetas").value(1))
                .andExpect(jsonPath("$.totalPlanos").value(1));

        mockMvc.perform(get("/relatorio/{userId}", anaId)
                        .with(auth(EMAIL_BRUNO)))
                .andExpect(status().isForbidden());
    }
}
