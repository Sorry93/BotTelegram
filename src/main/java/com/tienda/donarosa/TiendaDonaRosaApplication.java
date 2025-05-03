package com.tienda.donarosa;

import com.tienda.donarosa.config.TelegramConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Clase principal que inicia la aplicación Spring Boot de Tienda Doña Rosa.
 */
@SpringBootApplication // Indica que esta es una aplicación Spring Boot (habilita auto-configuración, escaneo de componentes, etc.)
@EnableConfigurationProperties(TelegramConfig.class) // Habilita la carga de propiedades específicas en la clase TelegramConfig
public class TiendaDonaRosaApplication {

    /**
     * Método principal que arranca la aplicación.
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(TiendaDonaRosaApplication.class, args); // Inicia el contexto de Spring y la aplicación
    }
}
