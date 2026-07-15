package com.decorcenter.dao;

import com.decorcenter.model.Usuario;

public interface UsuarioDAO {
    Usuario buscarPorEmail(String email);
    Usuario login(String email, String password);
    boolean registrar(Usuario usuario);
    boolean existeEmail(String email);
}
