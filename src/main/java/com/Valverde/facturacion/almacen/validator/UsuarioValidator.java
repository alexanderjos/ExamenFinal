package com.Valverde.facturacion.almacen.validator;

import com.Valverde.facturacion.almacen.entity.Usuario;
import com.Valverde.facturacion.almacen.exception.ValidateException;

public class UsuarioValidator {
	
	public static void save(Usuario registro) {
		if(registro.getEmail()==null || registro.getEmail().trim().isEmpty()) {
			throw new ValidateException("El nombre es requerido");
		}
		if(registro.getEmail().length()>70) {
			throw new ValidateException("El nombre no debe exceder los 70 caracteres");
		}
		if(registro.getPassword()==null || registro.getPassword().trim().isEmpty()) {
			throw new ValidateException("La contraseña es requerida");
		}

	}
	
}
