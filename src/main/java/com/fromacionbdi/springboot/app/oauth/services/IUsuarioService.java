package com.fromacionbdi.springboot.app.oauth.services;

import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;

public interface IUsuarioService {

    public Usuario findByUsername(String username);
}
