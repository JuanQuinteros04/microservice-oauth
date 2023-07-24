package com.fromacionbdi.springboot.app.oauth.security.event;


import com.fromacionbdi.springboot.app.oauth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

//import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;
import com.fromacionbdi.springboot.app.oauth.services.IUserService;

import feign.FeignException;


@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {


    @Autowired
    private IUserService usuarioService;

    private Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);


    @Override
    public void publishAuthenticationSuccess(Authentication authentication) {
//         if(authentication.getName().equalsIgnoreCase("frontendapp")) {
        if(authentication.getDetails() instanceof WebAuthenticationDetails) {
            return;
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String mensaje = "Success Login: " + userDetails.getUsername();
        System.out.println(mensaje);
        log.info(mensaje);

        User user = usuarioService.findUserByUsername(authentication.getName());

        if(user.getIntents() != null && user.getIntents() > 0) {
            user.setIntents(0);
            usuarioService.changeStateUser(user, user.getId());
        }
    }

    @Override
    public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
        String mensaje = "Error en el Login: " + exception.getMessage();
        log.error(mensaje);
        System.out.println(mensaje);

        try {

            StringBuilder errors = new StringBuilder();
            errors.append(mensaje);

            User user = usuarioService.findUserByUsername(authentication.getName());
            if (user.getIntents() == null) {
                user.setIntents(0);
            }

            log.info("Intentos actual es de: " + user.getIntents());

            user.setIntents(user.getIntents()+1);

            log.info("Intentos después es de: " + user.getIntents());

            errors.append(" - Intentos del login: " + user.getIntents());

            if(user.getIntents() >= 3) {
                String errorMaxIntentos = String.format("El usuario %s des-habilitado por máximos intentos.", user.getUsername());
                log.error(errorMaxIntentos);
                errors.append(" - " + errorMaxIntentos);
                user.setIsEnabled(false);
            }

            usuarioService.changeStateUser(user, user.getId());

        } catch (FeignException e) {
            log.error(String.format("El usuario %s no existe en el sistema", authentication.getName()));
        }

    }
}
