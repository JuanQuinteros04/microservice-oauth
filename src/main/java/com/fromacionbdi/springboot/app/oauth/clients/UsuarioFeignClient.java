package com.fromacionbdi.springboot.app.oauth.clients;

import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "servicio-usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/username/{username}")
    Usuario userForName(@RequestParam String username);

    @PutMapping("/modificar/{id}")
    Usuario editar(@RequestBody Usuario usuario, @PathVariable Long id);
}
