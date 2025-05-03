package com.tienda.donarosa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Clase de configuración para los datos del bot de Telegram.
 * 
 * Esta clase carga automáticamente las propiedades que comienzan con "telegram.bot"
 * definidas en el archivo application.properties o application.yml.
 */
@Configuration // Indica que esta clase es parte de la configuración de Spring
@ConfigurationProperties(prefix = "telegram.bot") // Carga propiedades con el prefijo "telegram.bot"
public class TelegramConfig {

    private String username; // Nombre de usuario del bot
    private String token;    // Token de autenticación del bot

    // Getter para obtener el nombre de usuario
    public String getUsername() {
        return username;
    }

    // Setter para establecer el nombre de usuario
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter para obtener el token
    public String getToken() {
        return token;
    }

    // Setter para establecer el token
    public void setToken(String token) {
        this.token = token;
    }
    
    private String chatId;

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

}
