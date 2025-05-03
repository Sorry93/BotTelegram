package com.tienda.donarosa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tienda.donarosa.model.Producto;

/**
 * Repositorio de acceso a datos para la entidad Producto.
 * 
 * Extiende JpaRepository para heredar métodos CRUD básicos como:
 * - findById
 * - findAll
 * - save
 * - deleteById
 * 
 * No es necesario implementar nada, Spring Data JPA genera automáticamente las consultas.
 */
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
