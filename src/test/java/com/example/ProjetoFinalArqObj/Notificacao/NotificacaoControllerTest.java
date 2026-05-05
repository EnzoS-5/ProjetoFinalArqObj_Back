package com.example.ProjetoFinalArqObj.Notificacao;

import com.example.ProjetoFinalArqObj.Habito.Habito;
import com.example.ProjetoFinalArqObj.User.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacaoController.class)
@AutoConfigureMockMvc(addFilters = false)
class NotificacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificacaoService notificacaoService;

    @Test
    void deveBuscarNotificacaoPorId() throws Exception {
        Notificacao notificacao = criarNotificacao();
        when(notificacaoService.buscarPorId(1)).thenReturn(notificacao);

        mockMvc.perform(get("/notificacao/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(2))
                .andExpect(jsonPath("$.habitoId").value(3))
                .andExpect(jsonPath("$.habitoTitulo").value("Beber agua"))
                .andExpect(jsonPath("$.visto").value(false))
                .andExpect(jsonPath("$.dataReferencia").value("2026-05-05"))
                .andExpect(jsonPath("$.ativo").value(true));
    }

    @Test
    void deveListarNotificacoesDoUsuarioLogado() throws Exception {
        when(notificacaoService.listarDoUsuarioLogado()).thenReturn(List.of(criarNotificacao()));

        mockMvc.perform(get("/notificacao"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].habitoTitulo").value("Beber agua"));
    }

    @Test
    void deveListarNotificacoesNaoVistas() throws Exception {
        when(notificacaoService.listarNaoVistasDoUsuarioLogado()).thenReturn(List.of(criarNotificacao()));

        mockMvc.perform(get("/notificacao/nao-vistas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].visto").value(false));
    }

    @Test
    void deveCriarNotificacao() throws Exception {
        Notificacao notificacao = criarNotificacao();
        when(notificacaoService.criar(3, LocalDate.of(2026, 5, 5))).thenReturn(notificacao);

        mockMvc.perform(post("/notificacao")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "habitoId": 3,
                                  "dataReferencia": "2026-05-05"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.habitoId").value(3));
    }

    @Test
    void deveMarcarNotificacaoComoVista() throws Exception {
        Notificacao notificacao = criarNotificacao();
        notificacao.setVisto(true);
        when(notificacaoService.marcarComoVista(1)).thenReturn(notificacao);

        mockMvc.perform(patch("/notificacao/1/visto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.visto").value(true));
    }

    @Test
    void deveDeletarNotificacao() throws Exception {
        mockMvc.perform(delete("/notificacao/1"))
                .andExpect(status().isNoContent());

        verify(notificacaoService).deletarLogicamente(1);
    }

    private Notificacao criarNotificacao() {
        User user = new User("Maria", "maria@email.com", "senha");
        user.setId(2);

        Habito habito = new Habito();
        habito.setId(3);
        habito.setUser(user);
        habito.setTitulo("Beber agua");
        habito.setDescricao("Tomar 2L");
        habito.setRegistroDiario(false);
        habito.setStreakInterno(4);
        habito.setAtivo(true);

        Notificacao notificacao = new Notificacao();
        notificacao.setId(1);
        notificacao.setUser(user);
        notificacao.setHabito(habito);
        notificacao.setVisto(false);
        notificacao.setDataReferencia(LocalDate.of(2026, 5, 5));
        notificacao.setAtivo(true);
        notificacao.setDataCriacao(LocalDateTime.of(2026, 5, 5, 8, 0));
        return notificacao;
    }
}
