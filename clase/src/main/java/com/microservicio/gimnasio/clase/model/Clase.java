package com.microservicio.gimnasio.clase.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDateTime horario;
    private int capacidadMaxima;
    private Long entrenadorId;

    @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asistente> asistentes;

    public Clase(Long id, String nombre, LocalDateTime horario, int capacidadMaxima) {
        this.id = id;
        this.nombre = nombre;
        this.horario = horario;
        this.capacidadMaxima = capacidadMaxima;
    }

    public String getFecha() {
        return horario.toLocalDate().toString();
    }
}
