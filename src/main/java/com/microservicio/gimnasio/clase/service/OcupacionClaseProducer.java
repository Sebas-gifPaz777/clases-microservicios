package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.dto.OcupacionClase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OcupacionClaseProducer {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void actualizarOcupacion(String claseId, int ocupacionActual) {
        OcupacionClase ocupacion = new OcupacionClase(claseId, ocupacionActual, LocalDateTime.now());
        kafkaTemplate.send("ocupacion-clases", claseId, "La ocupación actual es de: "+
                ocupacionActual +" para la clase con id: "+claseId);
    }
}