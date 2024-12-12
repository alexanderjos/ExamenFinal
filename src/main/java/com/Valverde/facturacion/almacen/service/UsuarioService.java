package com.Valverde.facturacion.almacen.service;

import java.util.List;
import org.springframework.data.domain.Pageable;

import com.Valverde.facturacion.almacen.entity.Usuario;


public interface UsuarioService {
    public List<Usuario> findAll();
	public List<Usuario> findAll(Pageable page);
    public Usuario findByEmail(String email);
    public Usuario findById(int id);
    public Usuario save(Usuario usuario);
    public Usuario deactivate(int id);
    public Usuario activate(int id);
}
