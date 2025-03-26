package com.microservicio.gimnasio.clase.controller;

import com.microservicio.gimnasio.clase.model.Asistente;
import com.microservicio.gimnasio.clase.model.Clase;
import com.microservicio.gimnasio.clase.model.InscripcionDTO;
import com.microservicio.gimnasio.clase.service.ClaseService;
import com.microservicio.gimnasio.clase.service.EventoEntrenamientoService;
import com.microservicio.gimnasio.clase.service.ResumenEntrenamientoConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RestController
@RequestMapping("/api/clase")
public class ClaseController {

    @Autowired
    ClaseService claseService;

    @Autowired
    private EventoEntrenamientoService eventoEntrenamientoService;

    @Autowired
    private ResumenEntrenamientoConsumer resumenConsumer;

    @PostMapping("/programar")
    public ResponseEntity<?> programarClase(@RequestBody Clase clase) {
        Clase claseR = claseService.programarClase(clase);
        return ResponseEntity.ok(claseR);
    }

    @PostMapping("/asignar/{id}")
    public ResponseEntity<?> agregarEntrenador(@RequestBody Long entrenadorId, @PathVariable Long id) {
        return ResponseEntity.ok(claseService.agregarEntrenador(entrenadorId, id));
    }

    @GetMapping("/listar")
    public ResponseEntity<?> obtenerLista() {
        return ResponseEntity.ok(claseService.obtenerTodasClases());
    }

    @PostMapping("/inscribirse/{miembroId}")
    public ResponseEntity<?> inscribirseaClase(@RequestBody InscripcionDTO claseId, @PathVariable Long miembroId) {
        try{
            Asistente asistente =  claseService.inscribirAsistente(claseId,miembroId);
            return ResponseEntity.ok(asistente);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/inventario/{invertarioId}")
    public ResponseEntity<?> asignarInventario(@RequestBody Long cantidad, @PathVariable Long inverntarioId) {
        try{
            claseService.escogerInventario(cantidad, inverntarioId);
            return ResponseEntity.ok("Cambio hecho");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Endpoint de prueba para publicar datos de entrenamiento
    @PostMapping("/datos-entrenamiento")
    public ResponseEntity<?> testDatosEntrenamiento(@RequestParam Long claseId, @RequestParam int cantidadAsistentes) {
        try {
            eventoEntrenamientoService.publicarDatosEntrenamiento(claseId, cantidadAsistentes);
            return ResponseEntity.ok("Mensaje de datos de entrenamiento enviado correctamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar mensaje: " + e.getMessage());
        }
    }

    

}
