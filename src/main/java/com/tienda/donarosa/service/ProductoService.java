package com.tienda.donarosa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tienda.donarosa.model.Producto;
import com.tienda.donarosa.repository.ProductoRepository;

/**
 * Servicio que gestiona las operaciones relacionadas con productos en la tienda.
 */
@Service
public class ProductoService {

    // Inyección automática del repositorio de productos
    @Autowired
    private ProductoRepository productoRepository;

    /**
     * Obtiene un producto por su código (ID).
     * @param codigo ID del producto a buscar
     * @return Optional que contiene el producto si existe, o vacío si no
     */
    public Optional<Producto> obtenerPorCodigo(Long codigo) {
        return productoRepository.findById(codigo);
    }

    /**
     * Guarda un nuevo producto o actualiza uno existente.
     * @param producto Producto a guardar o actualizar
     * @return Producto guardado
     */
    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    /**
     * Elimina un producto por su código.
     * @param codigo ID del producto a eliminar
     */
    public void eliminar(Long codigo) {
        productoRepository.deleteById(codigo);
    }

    /**
     * Obtiene la lista de todos los productos en el inventario.
     * @return Lista de productos
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    /**
     * Calcula el valor total del inventario (cantidad * precio de cada producto).
     * @return Valor total del inventario
     */
    public double calcularValorTotalInventario() {
        List<Producto> productos = obtenerTodosLosProductos();
        return productos.stream()
                .mapToDouble(producto -> producto.getCantidad() * producto.getPrecio())
                .sum();
    }

    /**
     * Actualiza la cantidad de un producto específico.
     * @param codigo ID del producto
     * @param nuevaCantidad Nueva cantidad a establecer
     * @return Producto actualizado, o null si no se encontró
     */
    public Producto actualizar(Long codigo, int nuevaCantidad) {
        Optional<Producto> productoOpt = productoRepository.findById(codigo);
        if (productoOpt.isPresent()) {
            Producto producto = productoOpt.get();
            producto.setCantidad(nuevaCantidad);
            return productoRepository.save(producto);
        }
        return null;
    }

    /**
     * Verifica si un producto está bajo el nivel mínimo de inventario.
     * @param producto Producto a evaluar
     * @return true si la cantidad es menor o igual a 5, false en caso contrario
     */
    public boolean isBajoMinimo(Producto producto) {
        return producto.getCantidad() <= 5;
    }

    /**
     * Obtiene todos los productos que tienen inventario bajo el mínimo permitido.
     * @return Lista de productos con inventario bajo
     */
    public List<Producto> obtenerProductosConBajoInventario() {
        return productoRepository.findAll().stream()
                .filter(this::isBajoMinimo)
                .toList();
    }
}
