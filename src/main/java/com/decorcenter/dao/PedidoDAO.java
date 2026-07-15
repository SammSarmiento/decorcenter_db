package com.decorcenter.dao;

import com.decorcenter.model.DetallePedido;
import com.decorcenter.model.Pedido;
import java.util.List;


public interface PedidoDAO {
    boolean crearPedido(Pedido pedido);
    List<Pedido> listarTodos();
}