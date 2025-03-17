package com.microservicio.gimnasio.clase.repository;

import com.microservicio.gimnasio.clase.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository extends JpaRepository<Clase,Long> {
}
