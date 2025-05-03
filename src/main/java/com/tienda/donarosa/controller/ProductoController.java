package com.tienda.donarosa.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;  //  Para controladores que renderizan vistas
import org.springframework.ui.Model;  //  Para pasar datos a las vistas
import org.springframework.web.bind.annotation.*;
import com.tienda.donarosa.model.Producto;
import com.tienda.donarosa.service.ProductoService;

//  @Controller en lugar de @RestController para poder devolver vistas y datos
@Controller
@RequestMapping("/api/productos")  //  Ruta base para todas las operaciones de productos
public class ProductoController {

    @Autowired  //  Inyección de dependencia del servicio
    private ProductoService productoService;
    
    //  API REST: Obtiene todos los productos (formato JSON)
    @GetMapping
    @ResponseBody  //  Importante: Indica que devuelve datos directamente, no una vista
    public List<Producto> obtenerTodosLosProductos() {
        return productoService.obtenerTodosLosProductos();
    }
    
    //  API REST: Obtiene un producto específico por código
    @GetMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Producto> obtenerPorCodigo(@PathVariable Long codigo) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        //  Devuelve 200 OK con el producto o 404 Not Found si no existe
        return producto.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    //  API REST: Crea un nuevo producto
    @PostMapping
    @ResponseBody
    public Producto crearProducto(@RequestBody Producto producto) {  //  @RequestBody convierte JSON a objeto
        return productoService.guardar(producto);
    }
    
    //  API REST: Actualiza un producto existente
    @PutMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Producto> actualizarProducto(
            @PathVariable Long codigo,  //  Código del producto de la URL
            @RequestBody Producto producto) {  //  Datos actualizados en formato JSON
        
        Optional<Producto> productoOpt = productoService.obtenerPorCodigo(codigo);
        if (productoOpt.isPresent()) {
            producto.setCodigo(codigo);  //  Asegura que el código sea el correcto
            return ResponseEntity.ok(productoService.guardar(producto));
        }
        return ResponseEntity.notFound().build();  //  404 si no existe
    }
    
    //  API REST: Elimina un producto
    @DeleteMapping("/{codigo}")
    @ResponseBody
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long codigo) {
        Optional<Producto> producto = productoService.obtenerPorCodigo(codigo);
        if (producto.isPresent()) {
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();  //  204 No Content = éxito sin contenido
        }
        return ResponseEntity.notFound().build();  //  404 si no existe
    }
    
    //  VISTA WEB: Muestra la página de inventario con datos
    @GetMapping("/inventario")
    public String mostrarInventario(Model model) {  //  Model para pasar datos a la vista
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        double valorTotalInventario = productoService.calcularValorTotalInventario();
        
        //  Agrega datos para que estén disponibles en la plantilla
        model.addAttribute("productos", productos);
        model.addAttribute("valorTotalInventario", valorTotalInventario);
        
        return "inventario";  //  Nombre de la plantilla Thymeleaf/JSP a renderizar
    }
}