package com.microservicio.gimnasio.clase.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguracion {

    @Bean
    public FanoutExchange intercambioClases() {
        return new FanoutExchange("clases.intercambio");
    }

    @Bean
    public Queue colaHorarios() {
        return new Queue("clases.horarios", true);
    }

    @Bean
    public Queue colaInscripciones() {
        return new Queue("clases.inscripciones", true);
    }

    @Bean
    public Binding enlaceHorarios(Queue colaHorarios, FanoutExchange intercambioClases) {
        return BindingBuilder.bind(colaHorarios).to(intercambioClases);
    }

    @Bean
    public Binding enlaceInscripciones(Queue colaInscripciones, FanoutExchange intercambioClases) {
        return BindingBuilder.bind(colaInscripciones).to(intercambioClases);
    }
}
