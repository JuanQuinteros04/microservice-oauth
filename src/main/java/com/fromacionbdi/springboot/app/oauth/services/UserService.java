package com.fromacionbdi.springboot.app.oauth.services;

import brave.Tracer;
//import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;
import com.fromacionbdi.springboot.app.oauth.clients.UsuarioFeignClient;
import com.fromacionbdi.springboot.app.oauth.model.User;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService, UserDetailsService {

    private Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UsuarioFeignClient client;

    @Autowired
    private Tracer tracer;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        try {
            ResponseEntity<User> userResponseEntity = client.getByUsername(username);
//            User user = client.userForName(username);

            if (userResponseEntity == null) {

                log.error("Error en el login, no existe el usuario '" + username + "' en el sistema");

                throw new UsernameNotFoundException("Error en el login, no existe el usuario '" + username + "' en el sistema");
            }

            User user = userResponseEntity.getBody();

            List<GrantedAuthority> authorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .peek(authority -> log.info("Role: " + authority.getAuthority()))
                    .collect(Collectors.toList());
            log.info("Usuario autenticado: " + username);

            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), user.getIsEnabled(), true, true, true,
                    authorities);
        }catch (FeignException e) {
            String error = "Error en el login, no existe el usuario '" + username + "' en el sistema";
            log.error("Error en el login, no existe el usuario '" + username + "' en el sistema");

            tracer.currentSpan().tag("error.mensaje", error + ": " + e.getMessage());
            throw new UsernameNotFoundException(
                    "Error en el login, no existe el usuario '" + username+ "' en el sistema");
        }
    }

    @Override
    public User findUserByUsername(String username) {
        ResponseEntity<User> userResponseEntity =  client.getByUsername(username);

        return userResponseEntity.getBody();
    }

    @Override
    public void changeStateUser(User user, Long id) {
         client.changeStateUser(user, id);
    }
}
