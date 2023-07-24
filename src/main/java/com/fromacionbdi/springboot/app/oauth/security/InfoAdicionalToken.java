package com.fromacionbdi.springboot.app.oauth.security;

//import com.formacionbdi.springboot.app.commons.usuarios.model.entity.Usuario;
import com.fromacionbdi.springboot.app.oauth.model.User;
import com.fromacionbdi.springboot.app.oauth.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InfoAdicionalToken implements TokenEnhancer {

    @Autowired
    private IUserService usuarioService;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        Map<String, Object> info = new HashMap<String, Object>();

        User user = usuarioService.findUserByUsername(oAuth2Authentication.getName());
        info.put("nombre", user.getName());
        info.put("apellido", user.getLastName());
        info.put("correo", user.getEmail());

        ((DefaultOAuth2AccessToken) oAuth2AccessToken).setAdditionalInformation(info);

        return oAuth2AccessToken;
    }
}
