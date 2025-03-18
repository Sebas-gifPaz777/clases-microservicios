package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.model.*;
import com.microservicio.gimnasio.clase.repository.AsistenteRepository;
import com.microservicio.gimnasio.clase.repository.ClaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    private ClaseRepository claseRepository;

    @Autowired
    private AsistenteRepository asistenteRepository;

    @Autowired
    private WebClient webClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Programa una nueva clase en la base de datos y envía una notificación a RabbitMQ.
     */
    OcupacionClaseProducer ocupacionClaseProducer;
  
    public Clase programarClase(Clase clase) {
        Clase nuevaClase = claseRepository.save(clase);

        // Enviar notificación a RabbitMQ sobre la nueva clase
        String mensaje = "Nueva clase programada: " + clase.getNombre() + " el " + clase.getFecha();
        rabbitTemplate.convertAndSend("clases.intercambio", "", mensaje);

        return nuevaClase;
    }

    /**
     * Asigna un entrenador a una clase consultando el servicio de entrenadores.
     */
    public Clase agregarEntrenador(Long entrenadorId, Long claseId) {
        Optional<Clase> claseOpt = claseRepository.findById(claseId);
        if (claseOpt.isEmpty()) {
            return null; // Retorna null si la clase no existe
        }

        Clase clase = claseOpt.get();

        // Consultar servicio de entrenadores
        Entrenador entrenador = webClient.get()
                .uri("http://localhost:8082/entrenadores/{id}", entrenadorId)
                .retrieve()
                .bodyToMono(Entrenador.class)
                .block();

        if (entrenador != null && entrenador.getDisponible()) {
            clase.setEntrenadorId(entrenadorId);
            entrenador.setDisponible(false);

            // Actualizar disponibilidad del entrenador
            webClient.put()
                    .uri("http://localhost:8082/entrenadores/{id}", entrenadorId)
                    .bodyValue(entrenador)
                    .retrieve()
                    .bodyToMono(Entrenador.class)
                    .block();

            return claseRepository.save(clase);
        }
        return null;
    }

    /**
     * Retorna todas las clases programadas.
     */
    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }


    /**
     * Inscribe un miembro en una clase, reduce la capacidad disponible y envía un evento a RabbitMQ.
     */
    public Asistente inscribirAsistente(InscripcionDTO claseId, Long miembroId) throws Exception{
        Asistente asistente = new Asistente(miembroId,claseId.getClaseId(),new Date());
        Optional<Clase> clase = claseRepository.findById(claseId.getClaseId());

        Miembro respuesta = webClient.get()
                .uri("http://localhost:8083/api/miembro/get/{miembroId}", miembroId)
                .headers(headers -> headers.setBearerAuth(claseId.getToken()))
                .retrieve()
                .bodyToMono(Miembro.class)
                .block();
        if (miembro == null || !miembro.getId().equals(miembroId)) {
            throw new Exception("El miembro con ID " + miembroId + " no existe.");
        }

        Clase clase = claseOpt.get();
        if (clase.getCapacidadMaxima() <= 0) {
            throw new Exception("No hay cupos disponibles para la clase " + clase.getNombre());
        if(clase.isPresent() && clase.get().getCapacidadMaxima()!=0 && respuesta.getId().equals(miembroId)){
            clase.get().setCapacidadMaxima(clase.get().getCapacidadMaxima()-1);
            String idClase = clase.get().getId().toString();
            int ocupacionActual = clase.get().getCapacidadMaxima();

            ocupacionClaseProducer.actualizarOcupacion(idClase,ocupacionActual);
            return asistenteRepository.save(asistente);

        }

        // Reducir capacidad disponible
        clase.setCapacidadMaxima(clase.getCapacidadMaxima() - 1);
        claseRepository.save(clase);

        // Crear y guardar el nuevo asistente de la clase
        Asistente asistente = new Asistente(miembroId, clase, new Date());
        Asistente nuevoAsistente = asistenteRepository.save(asistente);

        // Enviar notificación a RabbitMQ sobre la inscripción
        String mensaje = "El miembro " + miembro.getNombre() + " se inscribió en la clase " + clase.getNombre();
        rabbitTemplate.convertAndSend("clases.intercambio", "", mensaje);

        return nuevoAsistente;
    }

    /**
     * Actualiza el inventario de equipos consultando el servicio de equipos.
     */
    public void escogerInventario(Long cantidad, Long inventarioId) {
        webClient.put()
                .uri("http://localhost:8081/equipos/{inventarioId}/cantidad", inventarioId)
                .bodyValue(cantidad)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
