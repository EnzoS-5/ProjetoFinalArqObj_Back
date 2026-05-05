package com.example.ProjetoFinalArqObj;

import com.example.ProjetoFinalArqObj.Mascote.Mascote;
import com.example.ProjetoFinalArqObj.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UsuarioSecurityIntegrationTest extends IntegrationTestSupport {

    @Test
    void deveExigirAutenticacaoCriarMascotePadraoERejeitarEmailDuplicado() throws Exception {
        mockMvc.perform(get("/habito"))
                .andExpect(status().isUnauthorized());

        Integer userId = criarUsuario("Ana", EMAIL_ANA);

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "Ana 2",
                                  "email": " ana@email.com ",
                                  "senha": "outraSenha"
                                }
                                """))
                .andExpect(status().isConflict());

        User user = usuario(userId);
        Mascote mascote = mascoteRepository.findByUserAndAtivoTrue(user).orElseThrow();

        assertThat(mascote.getHp()).isEqualTo(100);
        assertThat(mascote.isCheck()).isFalse();
        assertThat(mascote.isAtivo()).isTrue();
    }

    @Test
    void deveBloquearAcessoAUsuarioEHabitoDeOutroDono() throws Exception {
        Integer anaId = criarUsuario("Ana", EMAIL_ANA);
        criarUsuario("Bruno", EMAIL_BRUNO);
        Integer habitoId = criarHabito(EMAIL_ANA, "Beber agua");

        mockMvc.perform(get("/usuarios/{id}", anaId)
                        .with(auth(EMAIL_BRUNO)))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/habito/{id}", habitoId)
                        .with(auth(EMAIL_BRUNO)))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/habito")
                        .with(auth(EMAIL_BRUNO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void deveExcluirUsuarioLogicamenteEDesativarDadosDependentes() throws Exception {
        Integer userId = criarUsuario("Ana", EMAIL_ANA);
        User user = usuario(userId);
        Integer habitoId = criarHabito(EMAIL_ANA, "Caminhar");
        Integer metaId = criarMeta(EMAIL_ANA, "Completar semana");
        criarPlano(EMAIL_ANA, "Plano semanal", habitoId, metaId);
        criarNotificacao(EMAIL_ANA, habitoId);

        mockMvc.perform(delete("/usuarios/{id}", userId)
                        .with(auth(EMAIL_ANA)))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findByEmailAndAtivoTrue(EMAIL_ANA)).isEmpty();
        assertThat(userRepository.findById(userId).orElseThrow().isAtivo()).isFalse();
        assertThat(habitoRepository.findAllByUserAndAtivoTrue(user)).isEmpty();
        assertThat(metaRepository.findAllByUserAndAtivoTrue(user)).isEmpty();
        assertThat(planoRepository.findAllByUserAndAtivoTrue(user)).isEmpty();
        assertThat(mascoteRepository.findAllByUserAndAtivoTrue(user)).isEmpty();
        assertThat(notificacaoRepository.findAllByUserAndAtivoTrueOrderByDataReferenciaDescDataCriacaoDesc(user)).isEmpty();

        mockMvc.perform(get("/usuarios/{id}", userId)
                        .with(auth(EMAIL_ANA)))
                .andExpect(status().isUnauthorized());
    }
}
