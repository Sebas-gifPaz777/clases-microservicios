package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.dto.ResumenEntrenamiento;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ResumenEntrenamientoConsumer {

    @KafkaListener(topics = "resumen-entrenamiento", groupId = "resumen-group")
    public void procesarResumen(ResumenEntrenamiento resumen) {
        System.out.println("Resumen recibido: " + resumen.getNumeroClases() + " clases, " +
                resumen.getTotalAsistentes() + " asistentes en la última semana.");
    }


}
