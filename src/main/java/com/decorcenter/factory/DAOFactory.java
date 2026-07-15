package com.decorcenter.factory;

import com.decorcenter.dao.CategoriaDAO;
import com.decorcenter.dao.PedidoDAO;
import com.decorcenter.dao.ProductoDAO;
import com.decorcenter.dao.UsuarioDAO;
import com.decorcenter.dao.impl.CategoriaDAOImpl;
import com.decorcenter.dao.impl.PedidoDAOImpl;
import com.decorcenter.dao.impl.ProductoDAOImpl;
import com.decorcenter.dao.impl.UsuarioDAOImpl;

/**
 * PATRÓN DE DISEÑO ADICIONAL: FACTORY METHOD
 * ------------------------------------------------------------
 * Centraliza la creación de los objetos DAO. Los servlets (controladores)
 * nunca instancian una implementación concreta con "new"; en su lugar
 * piden el DAO correspondiente a esta fábrica. Esto permite cambiar la
 * implementación (por ejemplo, pasar de MySQL a otra fuente de datos)
 * sin modificar los controladores.
 */
public class DAOFactory {

    private static final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();
    private static final ProductoDAO productoDAO = new ProductoDAOImpl();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAOImpl();
    private static final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    private DAOFactory() {
    }

    public static UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public static ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    public static CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public static PedidoDAO getPedidoDAO() {
        return pedidoDAO;
    }
}
