package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.dto.DatosEntrenamiento;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventoEntrenamientoService {

    @Qualifier("datosEntrenamientoKafkaTemplate")
    @Autowired
    private KafkaTemplate<String, DatosEntrenamiento> kafkaTemplate;

    public void publicarDatosEntrenamiento(Long claseId, int cantidadAsistentes) {
        DatosEntrenamiento datos = new DatosEntrenamiento(
                claseId,
               cantidadAsistentes,
                LocalDateTime.now()
        );
        // Publicar en el topic "datos-entrenamiento"
        kafkaTemplate.send("datos-entrenamiento", String.valueOf(claseId), datos);
        System.out.println("Publicado datos de entrenamiento para la clase " + claseId);
    }
}
