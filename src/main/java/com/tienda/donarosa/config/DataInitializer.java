package com.tienda.donarosa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.tienda.donarosa.model.Producto;
import com.tienda.donarosa.repository.ProductoRepository;

/**
 * Clase que inicializa la base de datos con datos predeterminados
 * al momento de arrancar la aplicación.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductoRepository productoRepository; // Repositorio de productos para operaciones CRUD
    
    /**
     * Este método se ejecuta automáticamente al iniciar la aplicación.
     * Se encarga de insertar datos de ejemplo si la base de datos está vacía.
     */
    @Override
    public void run(String... args) throws Exception {
        if (productoRepository.count() == 0) {
            // Solo inserta los productos si no hay registros existentes
            
            // Crear e insertar productos de ejemplo
            Producto p1 = new Producto();
            p1.setNombre("Peras");
            p1.setPrecio(4000);
            p1.setCantidad(65);
            productoRepository.save(p1);
            
            Producto p2 = new Producto();
            p2.setNombre("Limones");
            p2.setPrecio(1500);
            p2.setCantidad(25);
            productoRepository.save(p2);
            
            Producto p3 = new Producto();
            p3.setNombre("Moras");
            p3.setPrecio(2000);
            p3.setCantidad(30);
            productoRepository.save(p3);
            
            Producto p4 = new Producto();
            p4.setNombre("Piñas");
            p4.setPrecio(3000);
            p4.setCantidad(15);
            productoRepository.save(p4);
            
            Producto p5 = new Producto();
            p5.setNombre("Tomates");
            p5.setPrecio(1000);
            p5.setCantidad(30);
            productoRepository.save(p5);
            
            Producto p6 = new Producto();
            p6.setNombre("Fresas");
            p6.setPrecio(3000);
            p6.setCantidad(12);
            productoRepository.save(p6);
            
            Producto p7 = new Producto();
            p7.setNombre("Frunas");
            p7.setPrecio(300);
            p7.setCantidad(50);
            productoRepository.save(p7);
            
            Producto p8 = new Producto();
            p8.setNombre("Galletas");
            p8.setPrecio(500);
            p8.setCantidad(400);
            productoRepository.save(p8);
            
            Producto p9 = new Producto();
            p9.setNombre("Chocolates");
            p9.setPrecio(1200);
            p9.setCantidad(500);
            productoRepository.save(p9);
            
            Producto p10 = new Producto();
            p10.setNombre("Arroz");
            p10.setPrecio(1200);
            p10.setCantidad(60);
            productoRepository.save(p10);
            
            System.out.println("Base de datos inicializada con los productos de la tienda");
        }
    }
}
