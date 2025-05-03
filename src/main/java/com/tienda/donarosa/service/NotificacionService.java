package com.tienda.donarosa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.donarosa.bot.TiendaBot;
import com.tienda.donarosa.model.Producto;

import java.util.List;

/**
 * Servicio encargado de gestionar notificaciones relacionadas al inventario.
 * 
 * Actualmente, envía alertas de productos con bajo inventario a través de Telegram.
 */
@Service
public class NotificacionService {

    @Autowired
    private TiendaBot tiendaBot;  // Bot de Telegram para enviar mensajes

    /**
     * Notifica por Telegram los productos que tienen bajo inventario.
     * 
     * @param productos Lista de productos que cumplen la condición de bajo stock.
     */
    public void notificarBajoInventario(List<Producto> productos) {
        if (productos.isEmpty()) {
            // Si no hay productos con bajo inventario, no se envía notificación.
            return;
        }

        // Construye el mensaje a enviar
        StringBuilder mensaje = new StringBuilder("⚠️ *Productos con bajo inventario:*\n\n");
        for (Producto p : productos) {
            mensaje.append("- ").append(p.getNombre())
                   .append(": ").append(p.getCantidad()).append(" unidades\n");
        }

        // Envía el mensaje usando el bot de Telegram
        tiendaBot.enviaMensaje(mensaje.toString());
    }
}
