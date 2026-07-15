package com.decorcenter.dao;

import com.decorcenter.model.Producto;
import java.util.List;

public interface ProductoDAO {
    Producto buscarPorId(int id);
    boolean guardar(Producto producto);
    List<Producto> listarTodos(); // Cambiado para coincidir con el Servlet
    List<Producto> listarPorCategoria(int categoriaId);
    List<Producto> buscarPorNombre(String texto);
    boolean actualizar(Producto producto);
    boolean eliminar(int id);
    boolean actualizarStock(int productoId, int nuevoStock);
}
