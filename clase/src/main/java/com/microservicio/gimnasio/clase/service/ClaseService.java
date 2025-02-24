package com.microservicio.gimnasio.clase.service;

import com.microservicio.gimnasio.clase.model.Asistente;
import com.microservicio.gimnasio.clase.model.Clase;
import com.microservicio.gimnasio.clase.repository.AsistenteRepository;
import com.microservicio.gimnasio.clase.repository.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {

    @Autowired
    ClaseRepository claseRepository;

    @Autowired
    AsistenteRepository asistenteRepository;
    public Clase programarClase(Clase clase) {
       return claseRepository.save(clase);
    }
    public Clase agregarEntrenador(Long entrenadorId, Long id) {
        Optional<Clase> clase = claseRepository.findById(id);
        if(clase.isPresent())
            clase.get().setEntrenadorId(entrenadorId);
        return clase.get();
    }

    public List<Clase> obtenerTodasClases() {
        return claseRepository.findAll();
    }

    public Asistente inscribirAsistente(Long claseId, Long miembroId) throws Exception{
        Asistente asistente = new Asistente(miembroId,claseId,new Date());
        Optional<Clase> clase = claseRepository.findById(claseId);
        if(clase.isPresent() && clase.get().getCapacidadMaxima()!=0){
            clase.get().setCapacidadMaxima(clase.get().getCapacidadMaxima()-1);
            return asistenteRepository.save(asistente);
        }
        else
            throw new Exception("No hay mas cupos");

    }
}
