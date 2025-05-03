package com.tienda.donarosa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/**
 * Entidad Producto que representa un producto en la base de datos.
 */
@Entity // Marca la clase como una entidad JPA (persistida en la base de datos)
@Data // Lombok: genera automáticamente getters, setters, equals, hashCode y toString
public class Producto {

    @Id // Marca 'codigo' como la clave primaria de la entidad
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática incremental del ID (delegada a la base de datos)
    private Long codigo;

    private String nombre; // Nombre del producto
    private int precio;    // Precio del producto
    private int cantidad;  // Cantidad disponible en inventario

    /**
     * Constructor vacío requerido por JPA.
     */
    public Producto() {
    }

    /**
     * Constructor con parámetros para inicializar un Producto.
     *
     * @param nombre Nombre del producto
     * @param precio Precio del producto
     * @param cantidad Cantidad disponible del producto
     */
    public Producto(String nombre, int precio, int cantidad) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    // Getters y Setters explícitos (no necesarios si se usa @Data, pero incluidos por claridad)

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Método toString para representar el objeto como texto.
     */
    @Override
    public String toString() {
        return "Producto{" +
                "codigo=" + codigo +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                '}';
    }
}
