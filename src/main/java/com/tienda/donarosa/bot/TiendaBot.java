package com.tienda.donarosa.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Para inyectar valores de application.properties
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot; // Bot que maneja actualizaciones desde polling
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import com.tienda.donarosa.service.ProductoService;
import com.tienda.donarosa.model.Producto;

import java.util.Optional;
import java.util.List;

@Component  // Marca la clase como un componente de Spring para inyectar dependencias
public class TiendaBot extends TelegramLongPollingBot { // Hereda funcionalidad de bot Telegram

 @Value("${telegram.bot.username}") // Inyecta valor desde application.properties
 private String botUsername;

 @Value("${telegram.bot.token}") // Inyecta el token del bot desde application.properties
 private String botToken;

 @Value("${telegram.bot.defaultChatId}")  // Inyecta el ChatId con valor predeterminado si no existe
 private String defaultChatId;

     @Autowired // Inyecta para manipular prodcutos
     private ProductoService productoService;
     
     @Override  // Metodo requerido por la interfaz del bot
     public String getBotUsername() {
         return botUsername;
     }
     
     @Override
     public String getBotToken() {
         return botToken;
     }
     
     @Override // Metodo principal que maneja todas las actualizaciones requeridas
     public void onUpdateReceived(Update update) {
         if (update.hasMessage() && update.getMessage().hasText()) { // Verificacion de mensajes de texto
             String messageText = update.getMessage().getText();
             long chatId = update.getMessage().getChatId(); // ID unico de la conversacion
             
             String[] parts = messageText.split(" "); // Divide el mensaje en partes para extraer comando y parametros
             String command = parts[0].toLowerCase(); // Extrae el comando "primera palabra"
             
             try {
                 switch (command) { // Manejo de diferentes comandos
                     case "/start": // Comando inicial, muestra la ayuda
                         sendMessage(chatId, "¡Hola! Soy el bot de la tienda de Doña Rosa. Usa los comandos:\n" +
                                 "/insertar [nombre] [precio] [cantidad] - Para insertar un nuevo producto\n" +
                                 "/actualizar [código] [nuevaCantidad] - Para actualizar la cantidad de un producto\n" +
                                 "/borrar [código] - Para eliminar un producto\n" +
                        		 "/listar - Para ver el listado completo del inventario\n" +
                                 "/total - Para ver el valor total del inventario de un producto");
                         break;
                         
                     case "/insertar": // Comando para agregar un nuevo producto
                         if (parts.length >= 4) { // Verifica que tenga los parametros necesarios
                             try {
                                 String nombre = parts[1];
                                 int precio = Integer.parseInt(parts[2]); // Conversion de texto a numero
                                 int cantidad = Integer.parseInt(parts[3]);
                                 
                                 Producto nuevoProducto = new Producto(); // Creacion de un nuevo objeto Prodcuto
                                 nuevoProducto.setNombre(nombre);
                                 nuevoProducto.setPrecio(precio);
                                 nuevoProducto.setCantidad(cantidad);
                                 
                                 productoService.guardar(nuevoProducto);  //Almacena en la base de datos
                                 sendMessage(chatId, "Producto insertado correctamente: " + nombre);
                             } catch (NumberFormatException e) { // Manejo de errores de formato
                                 sendMessage(chatId, "Error en el formato de los datos. Use: /insertar [nombre] [precio] [cantidad]");
                             }
                         } else {
                             sendMessage(chatId, "Formato incorrecto. Use: /insertar [nombre] [precio] [cantidad]");
                         }
                         break;
                         
                     case "/actualizar": // Comando para modificar cantidad de producto existente
                         if (parts.length >= 3) {
                             try {
                                 Long codigo = Long.parseLong(parts[1]); // Conversion del codigo a Long
                                 int nuevaCantidad = Integer.parseInt(parts[2]);
                                 
                                 Producto producto = productoService.actualizar(codigo, nuevaCantidad); // Actualiza en DB
                                 if (producto != null) { // Verificacion de exito
                                     sendMessage(chatId, "Producto actualizado correctamente: " + producto.getNombre() + ", nueva cantidad: " + nuevaCantidad);
                                 } else {
                                     sendMessage(chatId, "No se encontró el producto con código: " + codigo);
                                 }
                             } catch (NumberFormatException e) {
                                 sendMessage(chatId, "Error en el formato de los datos. Use: /actualizar [código] [nuevaCantidad]");
                             }
                         } else {
                             sendMessage(chatId, "Formato incorrecto. Use: /actualizar [código] [nuevaCantidad]");
                         }
                         break;
                         
                     case "/listar": // Comando para mostrar todos los productos
                    	    try {
                    	        List<Producto> productos = productoService.obtenerTodosLosProductos(); 
                    	        
                    	        if (productos.isEmpty()) { // Verifica si hay productos
                    	            sendMessage(chatId, "No hay productos en el inventario.");
                    	        } else {
                    	            StringBuilder mensaje = new StringBuilder("📋 *INVENTARIO ACTUAL*\n\n"); // Formato con emojis
                    	            mensaje.append("ID | Nombre | Precio | Cantidad\n");
                    	            mensaje.append("----------------------------------\n");
                    	            
                    	            for (Producto p : productos) { // Recorre todos los productos
                    	                mensaje.append(p.getCodigo()).append(" | ")
                    	                       .append(p.getNombre()).append(" | ")
                    	                       .append(p.getPrecio()).append(" | ")
                    	                       .append(p.getCantidad()).append("\n");
                    	            }
                    	            
                    	            sendMessage(chatId, mensaje.toString()); // Envia el mensaje formateado
                    	        }
                    	    } catch (Exception e) {
                    	        sendMessage(chatId, "Error al obtener el inventario: " + e.getMessage());
                    	    }
                    	    break;
                    	    
                     case "/total": // Coamndo para calcular el total del inventario
                    	    try {
                    	        List<Producto> productos = productoService.obtenerTodosLosProductos();
                    	        
                    	        if (productos.isEmpty()) {
                    	            sendMessage(chatId, "No hay productos en el inventario.");
                    	        } else {
                    	            StringBuilder mensaje = new StringBuilder("💰 *VALOR DEL INVENTARIO*\n\n"); // Formato con emojis
                    	            mensaje.append("ID | Nombre | Precio | Cantidad | Valor Total\n");
                    	            mensaje.append("-----------------------------------------------\n");
                    	            
                    	            int valorInventarioTotal = 0; // Acumulador para el valor total
                    	            
                    	            for (Producto p : productos) {
                    	                int valorProducto = p.getPrecio() * p.getCantidad(); // Calculo valor por producto
                    	                valorInventarioTotal += valorProducto; // Suma al total
                    	                
                    	                mensaje.append(p.getCodigo()).append(" | ")
                    	                       .append(p.getNombre()).append(" | ")
                    	                       .append(p.getPrecio()).append(" | ")
                    	                       .append(p.getCantidad()).append(" | ")
                    	                       .append(valorProducto).append("\n");
                    	            }
                    	            
                    	            mensaje.append("\n*VALOR TOTAL DEL INVENTARIO: $").append(valorInventarioTotal).append("*"); // Muestra el total
                    	            
                    	            sendMessage(chatId, mensaje.toString());
                    	        }
                    	    } catch (Exception e) {
                    	        sendMessage(chatId, "Error al calcular el valor del inventario: " + e.getMessage());
                    	    }
                    	    break;
                         
                     case "/borrar": // Comando para eliminar productos
                         if (parts.length >= 2) {
                             try {
                                 Long codigo = Long.parseLong(parts[1]);
                                 Optional<Producto> productoOpt = productoService.obtenerPorCodigo(codigo); // Busca primero el producto
                                 
                                 if (productoOpt.isPresent()) { // Verifica existencia antes de eliminar
                                     String nombreProducto = productoOpt.get().getNombre();
                                     productoService.eliminar(codigo); // Elimina de la DB
                                     sendMessage(chatId, "Producto eliminado correctamente: " + nombreProducto);
                                 } else {
                                     sendMessage(chatId, "No se encontró el producto con código: " + codigo);
                                 }
                             } catch (NumberFormatException e) {
                                 sendMessage(chatId, "Error en el formato del código. Use: /borrar [código]");
                             }
                         } else {
                             sendMessage(chatId, "Formato incorrecto. Use: /borrar [código]");
                         }
                         break;
                         
                     default: // Manejo de comando desconocidos
                         sendMessage(chatId, "Comando no reconocido. Use /start para ver las opciones disponibles.");
                 }
             } catch (Exception e) { // Manejo general de excepciones
                 try {
                     sendMessage(chatId, "Ha ocurrido un error: " + e.getMessage());
                 } catch (TelegramApiException ex) {
                     ex.printStackTrace();
                 }
             }
         }
     }
     

 // Método auxiliar para enviar mensajes a usuarios especificos
 private void sendMessage(long chatId, String text) throws TelegramApiException {
     SendMessage message = new SendMessage();
     message.setChatId(String.valueOf(chatId)); // Convierte Long a String para el ChatID 
     message.setText(text);
     execute(message); // Ejecuta el envio del mensaje
 } 

 // Metodo publico para enviar notificaciones desde otros servicios
 public void enviaMensaje(String texto) {
     SendMessage message = new SendMessage();
     message.setChatId(defaultChatId); // Chat ID definido en el archivo application.properties
     message.setText(texto);
     try {
         execute(message);
     } catch (TelegramApiException e) {
         e.printStackTrace(); // O usa un logger
     }
 }
}
