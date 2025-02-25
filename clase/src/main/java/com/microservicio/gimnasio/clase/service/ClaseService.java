package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.model.Asistente;
import com.microservicio.gimnasio.clase.model.Clase;
import com.microservicio.gimnasio.clase.model.Entrenador;
import com.microservicio.gimnasio.clase.model.Miembro;
import com.microservicio.gimnasio.clase.repository.AsistenteRepository;
import com.microservicio.gimnasio.clase.repository.ClaseRepository;
import jakarta.transaction.Transactional;
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

    /**
     * Creates or updates a Clase in the database.
     */
    public Clase programarClase(Clase clase) {
        return claseRepository.save(clase);
    }

    /**
     * Associates an entrenador with a Clase by calling
     * an external service (entrenadores) via WebClient.
     */
    public Clase agregarEntrenador(Long entrenadorId, Long claseId) {
        Optional<Clase> claseOpt = claseRepository.findById(claseId);
        if (claseOpt.isEmpty()) {
            return null; // or throw an exception
        }

        Clase clase = claseOpt.get();
        // Retrieve entrenador data from external service
        Entrenador entrenador = webClient.get()
                .uri("http://localhost:8082/entrenadores/{id}", entrenadorId)
                .retrieve()
                .bodyToMono(Entrenador.class)
                .block();

        if (entrenador != null && entrenador.getDisponible()) {
            clase.setEntrenadorId(entrenadorId);
            entrenador.setDisponible(false);

            // Update entrenador availability
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
     * Returns all scheduled classes.
     */
    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }

    /**
     * Enrolls a member (miembroId) into a given Clase (claseId).
     * Checks capacity, calls another service to verify Miembro,
     * and reduces capacity if successful.
     */
    @Transactional
    public Asistente inscribirAsistente(Long claseId, Long miembroId) throws Exception {
        Optional<Clase> claseOpt = claseRepository.findById(claseId);
        if (claseOpt.isEmpty()) {
            throw new Exception("La clase con ID " + claseId + " no existe.");
        }

        // Check if member actually exists in the external service
        Miembro miembro = webClient.get()
                .uri("http://localhost:8083/entrenadores/{miembroId}", miembroId)
                .retrieve()
                .bodyToMono(Miembro.class)
                .block();

        if (miembro == null || !miembro.getId().equals(miembroId)) {
            throw new Exception("El miembro con ID " + miembroId + " no existe o no coincide.");
        }

        Clase clase = claseOpt.get();
        if (clase.getCapacidadMaxima() <= 0) {
            throw new Exception("No hay cupos disponibles para la clase " + clase.getNombre());
        }

        // Decrease capacity
        clase.setCapacidadMaxima(clase.getCapacidadMaxima() - 1);
        claseRepository.save(clase);

        // Create and save the new Asistente linked to the Clase
        Asistente asistente = new Asistente(miembroId, clase, new Date());
        return asistenteRepository.save(asistente);
    }

    /**
     * Example of updating inventory (equipos) via WebClient.
     */
    public void escogerInventario(Long cantidad, Long inventarioId) {
        webClient.put()
                .uri("http://localhost:8081/equipos/{inventarioId}/cantidad", inventarioId)
                .bodyValue(cantidad)
                .retrieve()
                .bodyToMono(Entrenador.class)
                .block();
    }
}
