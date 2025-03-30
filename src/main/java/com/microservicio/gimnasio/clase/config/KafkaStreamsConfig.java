package com.microservicio.gimnasio.clase.config;

import com.microservicio.gimnasio.clase.dto.DatosEntrenamiento;
import com.microservicio.gimnasio.clase.dto.ResumenEntrenamiento;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@EnableKafkaStreams
public class KafkaStreamsConfig {

    @Bean
    public KStream<String, DatosEntrenamiento> kStream(StreamsBuilder streamsBuilder) {
        KStream<String, DatosEntrenamiento> stream = streamsBuilder.stream("datos-entrenamiento");

        stream.groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(7)))
                .aggregate(
                        ResumenEntrenamiento::new,
                        (key, value, aggregate) -> aggregate.actualizar(value),
                        Materialized.as("resumen-entrenamiento-store")
                )
                .toStream()
                .to("resumen-entrenamiento");
        return stream;
    }
}
