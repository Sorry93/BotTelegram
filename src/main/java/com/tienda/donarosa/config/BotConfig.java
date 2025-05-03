package com.tienda.donarosa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import com.tienda.donarosa.bot.TiendaBot;

/**
 * Configuración para registrar y habilitar el bot de Telegram en la aplicación.
 */
@Configuration // Indica que esta clase contiene definiciones de Beans para el contexto de Spring
public class BotConfig {

    /**
     * Bean que configura e inicia el API de bots de Telegram y registra el bot TiendaBot.
     *
     * @param tiendaBot Instancia del bot que se va a registrar
     * @return Instancia de TelegramBotsApi con el bot registrado
     * @throws TelegramApiException si ocurre un error durante el registro del bot
     */
    @Bean
    public TelegramBotsApi telegramBotsApi(TiendaBot tiendaBot) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class); // Inicializa la sesión por defecto
        botsApi.registerBot(tiendaBot); // Registra el bot en la API de Telegram
        return botsApi;
    }
}
