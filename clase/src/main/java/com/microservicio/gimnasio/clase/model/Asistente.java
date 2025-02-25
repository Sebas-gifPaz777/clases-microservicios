package com.microservicio.gimnasio.clase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Asistente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long miembroId;
    private Date fechaInscripcion;  // renamed without accent for best practice

    @ManyToOne
    @JoinColumn(name = "clase_id")
    private Clase clase;

    public Asistente(Long miembroId, Clase clase, Date fechaInscripcion) {
        this.miembroId = miembroId;
        this.clase = clase;
        this.fechaInscripcion = fechaInscripcion;
    }
}
