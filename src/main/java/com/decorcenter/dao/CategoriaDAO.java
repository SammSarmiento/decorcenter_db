package com.decorcenter.dao;

import com.decorcenter.model.Categoria;
import java.util.List;

public interface CategoriaDAO {

    List<Categoria> listarTodas();

    Categoria buscarPorId(int id);

    boolean guardar(Categoria categoria);

    boolean actualizar(Categoria categoria);

    boolean eliminar(int id);
}
