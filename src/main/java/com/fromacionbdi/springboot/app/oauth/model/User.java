package com.fromacionbdi.springboot.app.oauth.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {

    private Long id;

    private String username;

    private String password;

    private Boolean isEnabled;

    private String name;

    private String lastName;

    private String email;

    private List<Role> roles;

    private Integer intents;

}
