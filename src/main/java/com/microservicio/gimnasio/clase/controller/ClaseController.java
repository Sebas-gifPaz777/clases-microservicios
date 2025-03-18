package com.microservicio.gimnasio.clase.controller;

import com.microservicio.gimnasio.clase.model.Asistente;
import com.microservicio.gimnasio.clase.model.Clase;
import com.microservicio.gimnasio.clase.model.InscripcionDTO;
import com.microservicio.gimnasio.clase.service.ClaseService;
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
}
