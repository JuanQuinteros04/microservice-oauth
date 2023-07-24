package com.fromacionbdi.springboot.app.oauth.services;

import com.fromacionbdi.springboot.app.oauth.model.User;

public interface IUserService {

    User findUserByUsername(String username);

    void changeStateUser(User user, Long id);

//    public Usuario findByUsername(String username);
//
//    public Usuario update(Usuario usuario, Long id);
}
