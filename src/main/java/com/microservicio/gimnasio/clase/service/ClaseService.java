package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.model.*;
import com.microservicio.gimnasio.clase.repository.AsistenteRepository;
import com.microservicio.gimnasio.clase.repository.ClaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    WebClient webClient;

    @Autowired
    AsistenteRepository asistenteRepository;

    @Autowired
    OcupacionClaseProducer ocupacionClaseProducer;


    public Clase programarClase(Clase clase) {
        //clase.setAsistente(new ArrayList<>());
       return claseRepository.save(clase);
    }
    public Clase agregarEntrenador(Long entrenadorId, Long id) {
        Optional<Clase> clase = claseRepository.findById(id);
        if(clase.isPresent()){
            Entrenador entrenador = webClient.get()
                    .uri("http://localhost:8082/entrenadores/{id}", id)
                    .retrieve()
                    .bodyToMono(Entrenador.class)
                    .block();
            if(entrenador.getDisponible()){
                clase.get().setEntrenadorId(id);
                entrenador.setDisponible(false);
                webClient.put()
                        .uri("http://localhost:8082/entrenadores/{id}", id) // Pasar ID en la URL
                        .bodyValue(entrenador) // Enviar el objeto en el cuerpo de la solicitud
                        .retrieve()
                        .bodyToMono(Entrenador.class) // Convertir la respuesta a un objeto Entrenador
                        .block();
            }

            claseRepository.save(clase.get());
        }
            return null;
    }

    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }

    public Asistente inscribirAsistente(InscripcionDTO claseId, Long miembroId) throws Exception{
        Asistente asistente = new Asistente(miembroId,claseId.getClaseId(),new Date());
        Optional<Clase> clase = claseRepository.findById(claseId.getClaseId());

        Miembro respuesta = webClient.get()
                .uri("http://localhost:8083/api/miembro/get/{miembroId}", miembroId)
                .headers(headers -> headers.setBearerAuth(claseId.getToken()))
                .retrieve()
                .bodyToMono(Miembro.class)
                .block();

        if(clase.isPresent() && clase.get().getCapacidadMaxima()!=0 && respuesta.getId().equals(miembroId)){
            clase.get().setCapacidadMaxima(clase.get().getCapacidadMaxima()-1);
            String idClase = clase.get().getId().toString();
            int ocupacionActual = clase.get().getCapacidadMaxima();

            ocupacionClaseProducer.actualizarOcupacion(idClase,ocupacionActual);
            return asistenteRepository.save(asistente);
        }
        else
            throw new Exception("No hay mas cupos");

    }

    public void escogerInventario(Long cantidad, Long inventarioId) {
        webClient.put()
                .uri("http://localhost:8081/equipos/{inventarioId}/cantidad", inventarioId) // Pasar ID en la URL
                .bodyValue(cantidad) // Enviar el objeto en el cuerpo de la solicitud
                .retrieve()
                .bodyToMono(Entrenador.class) // Convertir la respuesta a un objeto Entrenador
                .block();

    }
}
