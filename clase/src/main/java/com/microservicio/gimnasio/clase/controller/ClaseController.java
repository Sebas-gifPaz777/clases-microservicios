package com.microservicio.gimnasio.clase.controller;

import com.microservicio.gimnasio.clase.model.Asistente;
import com.microservicio.gimnasio.clase.model.Clase;
import com.microservicio.gimnasio.clase.service.ClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clase")
public class ClaseController {

    @Autowired
    ClaseService claseService;

    @PostMapping("/programar")
    public Clase programarClase(@RequestBody Clase clase) {
        return claseService.programarClase(clase);
    }

    @PostMapping("/asignar")
    public Clase agregarEntrenador(@RequestBody Long entrenadorId, @RequestParam Long id) {
        return claseService.agregarEntrenador(entrenadorId, id);
    }

    @GetMapping("/listar")
    public List<Clase> obtenerLista() {
        return claseService.obtenerTodasClases();
    }

    @PostMapping("/inscribirse")
    public ResponseEntity<?> inscribirseaClase(@RequestBody Long claseId, @RequestParam Long miembroId) {
        try{
            Asistente asistente =  claseService.inscribirAsistente(claseId,miembroId);
            return ResponseEntity.ok(asistente);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
