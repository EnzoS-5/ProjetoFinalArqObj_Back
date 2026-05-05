package com.example.ProjetoFinalArqObj;

import com.example.ProjetoFinalArqObj.Habito.HabitoRepository;
import com.example.ProjetoFinalArqObj.Habito.HabitoService;
import com.example.ProjetoFinalArqObj.Mascote.MascoteRepository;
import com.example.ProjetoFinalArqObj.Mascote.MascoteService;
import com.example.ProjetoFinalArqObj.Meta.MetaRepository;
import com.example.ProjetoFinalArqObj.Notificacao.NotificacaoRepository;
import com.example.ProjetoFinalArqObj.Plano.PlanoRepository;
import com.example.ProjetoFinalArqObj.User.User;
import com.example.ProjetoFinalArqObj.User.UserRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.time.LocalDate;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
abstract class IntegrationTestSupport {

    protected static final String SENHA = "senha123";
    protected static final String EMAIL_ANA = "ana@email.com";
    protected static final String EMAIL_BRUNO = "bruno@email.com";
    protected static final LocalDate DATA_REFERENCIA = LocalDate.of(2026, 5, 5);

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected HabitoRepository habitoRepository;

    @Autowired
    protected MetaRepository metaRepository;

    @Autowired
    protected PlanoRepository planoRepository;

    @Autowired
    protected MascoteRepository mascoteRepository;

    @Autowired
    protected NotificacaoRepository notificacaoRepository;

    @Autowired
    protected MascoteService mascoteService;

    @Autowired
    protected HabitoService habitoService;

    @BeforeEach
    void limparBanco() {
        notificacaoRepository.deleteAll();
        planoRepository.deleteAll();
        metaRepository.deleteAll();
        habitoRepository.deleteAll();
        mascoteRepository.deleteAll();
        userRepository.deleteAll();
    }

    protected RequestPostProcessor auth(String email) {
        return httpBasic(email, SENHA);
    }

    protected User usuario(Integer id) {
        return userRepository.findById(id).orElseThrow();
    }

    protected Integer criarUsuario(String nome, String email) throws Exception {
        MvcResult result = mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nome": "%s",
                                  "email": "%s",
                                  "senha": "%s"
                                }
                                """.formatted(nome, email, SENHA)))
                .andExpect(status().isCreated())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    protected Integer criarHabito(String email, String titulo) throws Exception {
        MvcResult result = mockMvc.perform(post("/habito")
                        .with(auth(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "%s"
                                }
                                """.formatted(titulo)))
                .andExpect(status().isCreated())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    protected Integer criarMeta(String email, String titulo) throws Exception {
        MvcResult result = mockMvc.perform(post("/meta")
                        .with(auth(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "%s"
                                }
                                """.formatted(titulo)))
                .andExpect(status().isCreated())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    protected Integer criarPlano(String email, String titulo, Integer habitoId, Integer metaId) throws Exception {
        MvcResult result = mockMvc.perform(post("/plano")
                        .with(auth(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "titulo": "%s",
                                  "habitoIds": [%d],
                                  "metaId": %d
                                }
                                """.formatted(titulo, habitoId, metaId)))
                .andExpect(status().isCreated())
                .andReturn();

        return JsonPath.read(result.getResponse().getContentAsString(), "$.id");
    }

    protected void criarNotificacao(String email, Integer habitoId) throws Exception {
        mockMvc.perform(post("/notificacao")
                        .with(auth(email))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "habitoId": %d,
                                  "dataReferencia": "%s"
                                }
                                """.formatted(habitoId, DATA_REFERENCIA)))
                .andExpect(status().isCreated());
    }

    protected void salvarPontuacao(User user, int xp, int streak) {
        user.setXp(xp);
        user.setStreak(streak);
        userRepository.saveAndFlush(user);
    }
}
