package com.fromacionbdi.springboot.app.oauth.clients;

import com.fromacionbdi.springboot.app.oauth.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "service-users")
public interface UsuarioFeignClient {

    @GetMapping("/users/username/{username}")
    ResponseEntity<User> getByUsername(@PathVariable("username") String username);

    @PutMapping("/users/{id}")
    User changeStateUser(@RequestBody User user, @PathVariable("id") Long id);

}
