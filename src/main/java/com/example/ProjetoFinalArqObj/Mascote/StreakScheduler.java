package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.Habito.HabitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;

@Component
public class StreakScheduler {
    private static final String SAO_PAULO_ZONE_ID = "America/Sao_Paulo";
    private static final ZoneId SAO_PAULO_ZONE = ZoneId.of(SAO_PAULO_ZONE_ID);

    @Autowired
    private MascoteService mascoteService;

    @Autowired
    private HabitoService habitoService;

    @Scheduled(cron = "0 59 23 * * *", zone = SAO_PAULO_ZONE_ID)
    public void executarRotinaDiaria() {
        LocalDate dataReferencia = LocalDate.now(SAO_PAULO_ZONE);
        mascoteService.verificarEReduzirHPPorStreakNaoAtualizado(dataReferencia);
        habitoService.resetDiario(dataReferencia);
    }
}
