package com.example.ProjetoFinalArqObj;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.Mascote.Mascote;
import com.example.ProjetoFinalArqObj.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProgressoIntegrationTest extends IntegrationTestSupport {

    @Test
    void deveAplicarLimitesXpStreakENovosLimitesAposSubirNivel() throws Exception {
        Integer userId = criarUsuario("Ana", EMAIL_ANA);
        Integer habitoId = criarHabito(EMAIL_ANA, "Beber agua");
        criarHabito(EMAIL_ANA, "Caminhar");

        mockMvc.perform(post("/habito")
                        .with(auth(EMAIL_ANA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Ler"
                                }
                                """))
                .andExpect(status().isBadRequest());

        mockMvc.perform(patch("/habito/{id}/registro-diario", habitoId)
                        .with(auth(EMAIL_ANA)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.registroDiario").value(true))
                .andExpect(jsonPath("$.streakInterno").value(1));

        User user = usuario(userId);
        assertThat(user.getXp()).isEqualTo(10);
        assertThat(user.getStreak()).isEqualTo(1);

        user.adicionarXp(4990);
        userRepository.saveAndFlush(user);

        User usuarioAtualizado = usuario(userId);
        assertThat(usuarioAtualizado.getNivel()).isEqualTo(5);
        assertThat(usuarioAtualizado.getMaxHabitos()).isEqualTo(3);
        assertThat(usuarioAtualizado.getMaxMetas()).isEqualTo(2);
        assertThat(usuarioAtualizado.getMaxPlano()).isEqualTo(2);

        mockMvc.perform(post("/habito")
                        .with(auth(EMAIL_ANA))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "Ler"
                                }
                                """))
                .andExpect(status().isCreated());
    }

    @Test
    void rotinaDiariaDeveReduzirHpResetarStreakEGerarNotificacao() throws Exception {
        Integer userId = criarUsuario("Ana", EMAIL_ANA);
        User user = usuario(userId);
        Integer habitoId = criarHabito(EMAIL_ANA, "Caminhar");
        Habito habito = habitoRepository.findById(habitoId).orElseThrow();
        Mascote mascote = mascoteRepository.findByUserAndAtivoTrue(user).orElseThrow();

        user.setStreak(3);
        habito.setStreakInterno(4);
        userRepository.saveAndFlush(user);
        habitoRepository.saveAndFlush(habito);

        mascoteService.verificarEReduzirHPPorStreakNaoAtualizado(DATA_REFERENCIA);
        habitoService.resetDiario(DATA_REFERENCIA);

        assertThat(mascoteRepository.findById(mascote.getId()).orElseThrow().getHp()).isEqualTo(90);
        assertThat(userRepository.findById(userId).orElseThrow().getStreak()).isZero();
        assertThat(habitoRepository.findById(habitoId).orElseThrow().getStreakInterno()).isZero();
        assertThat(notificacaoRepository.findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(user))
                .hasSize(1);
    }
}
