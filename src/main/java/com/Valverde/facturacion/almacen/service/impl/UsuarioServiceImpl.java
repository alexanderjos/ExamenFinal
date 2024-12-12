package com.Valverde.facturacion.almacen.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Valverde.facturacion.almacen.entity.Rol;
import com.Valverde.facturacion.almacen.entity.Usuario;
import com.Valverde.facturacion.almacen.exception.GeneralException;
import com.Valverde.facturacion.almacen.exception.NoDataFoundException;
import com.Valverde.facturacion.almacen.exception.ValidateException;
import com.Valverde.facturacion.almacen.repository.UsuarioRepository;
import com.Valverde.facturacion.almacen.service.UsuarioService;
import com.Valverde.facturacion.almacen.util.Encoder;
import com.Valverde.facturacion.almacen.validator.UsuarioValidator;
@Service

public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private Encoder encoder;

    @Autowired
    private UsuarioRepository repository;
    @Override
    @Transactional(readOnly = true)
    public List < Usuario > findAll() {
        try {
            List < Usuario > registros = repository.findAll();
            return registros;
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw new GeneralException("Error del servidor");
        }
    }
    @Override
    @Transactional(readOnly = true)
    public List < Usuario > findAll(Pageable page) {
        try {
            List < Usuario > registros = repository.findAll(page).toList();
            return registros;
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByEmail(String email) {
        try {
            Usuario registro = repository.findByEmail(email);
            if (registro == null) {
                throw new NoDataFoundException("Error en el servidor");
            }
            return registro;
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(int id) {
        try {
            Usuario registro = repository.findById(id).
                orElseThrow(()->new NoDataFoundException("Error en el servidor"));
            if (registro == null) {
                throw new NoDataFoundException("Error en el servidor");
            }
            return registro;
        } catch (ValidateException | NoDataFoundException e) {
            throw e;
        } catch (GeneralException e) {
            throw new GeneralException("Error del servidor");
        }
    }

    @Override
    @Transactional
    public Usuario save(Usuario usuario) {
        try {
			
			UsuarioValidator.save(usuario);
			//Nuevo registro
			if (usuario.getId()==0) {
                usuario.setActivo(true);
                usuario.setPassword(encoder.encode(usuario.getPassword()));
				Set<Rol> roles = usuario.getRoles();
				if (roles != null && !roles.isEmpty()) {
                    usuario.setRoles(roles);
                  
                }
                Usuario nuevo=repository.save(usuario);
                return nuevo;
            }

			//editar registro
			Usuario registro=repository.findById(usuario.getId()).orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
			registro.setEmail(usuario.getEmail());
            registro.setPassword(encoder.encode(usuario.getPassword()));
			registro.setRoles(usuario.getRoles());
            repository.save(registro);
			return registro;
		} catch (ValidateException | NoDataFoundException e) {
			throw e;
		}
		catch (GeneralException e) {
			throw new GeneralException("Error del servidor");
		}
    }

    @Override
    @Transactional
    public Usuario deactivate(int id) {
       try{
            Usuario registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
            registro.setActivo(false);
            repository.save(registro);
            return registro;
       }
         catch (ValidateException | NoDataFoundException e) {
            throw e;
         }
            catch (GeneralException e) {
                throw new GeneralException("Error del servidor");
            }
    }

    @Override
    @Transactional
    public Usuario activate(int id) {
        try{
            Usuario registro=repository.findById(id).orElseThrow(()->new NoDataFoundException("No existe un registro con ese ID"));
            registro.setActivo(true);
            repository.save(registro);
            return registro;
       }
         catch (ValidateException | NoDataFoundException e) {
            throw e;
         }
            catch (GeneralException e) {
                throw new GeneralException("Error del servidor");
            }
    }

}
