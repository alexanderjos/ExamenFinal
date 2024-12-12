package com.Valverde.facturacion.almacen.service.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.Valverde.facturacion.almacen.entity.Usuario;
import com.Valverde.facturacion.almacen.service.UsuarioService;

import jakarta.transaction.Transactional;

@Service
public class LoginServiceImpl implements UserDetailsService {
    @Autowired
    private UsuarioService service;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Usuario usuario = service.findByEmail(username);
       if (usuario == null) {
           throw new UsernameNotFoundException("Usuario no encontrado con el email: " + username);
       }
       //Mapear los roles del usuario
       List<GrantedAuthority> authorities = usuario.getRoles().stream()
               .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getNombre()))
               .collect(Collectors.toList());
        return User.withUsername(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(authorities) //Usar la lista de auriidades generadas
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!usuario.isActivo()) //Verificar si el usuario esta activo
                .build();
    }
   

}
