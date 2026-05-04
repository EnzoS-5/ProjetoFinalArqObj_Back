package com.example.ProjetoFinalArqObj.Mascote;

import com.example.ProjetoFinalArqObj.Habito.HabitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StreakScheduler {

    @Autowired
    private MascoteService mascoteService;

    @Autowired
    private HabitoService habitoService;

    @Scheduled(cron = "0 59 23 * * *")
    public void verificarStreakDiario() {
        mascoteService.verificarEReduzirHPPorStreakNaoAtualizado();
        habitoService.resetarStreakSeRegistroDiarioFalso();
    }
}
