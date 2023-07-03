package com.fromacionbdi.springboot.app.oauth.clients;

import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "servicio-usuarios")
public interface UsuarioFeignClient {

    @GetMapping("/username/{username}")
    Usuario findByUsername(@RequestParam String username);
}
