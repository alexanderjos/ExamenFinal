package com.Valverde.facturacion.almacen.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.Valverde.facturacion.almacen.dto.RolDto;
import com.Valverde.facturacion.almacen.dto.UsuarioDto;
import com.Valverde.facturacion.almacen.entity.Usuario;

@Component
public class UsuarioConverter extends AbstractConverter<Usuario, UsuarioDto> {

	@Override
	public UsuarioDto fromEntity(Usuario entity) {
		if(entity==null) return null;
		List<RolDto> rolesDto = entity.getRoles().stream()
			.map(rol->new RolDto(rol.getId(),rol.getNombre()))
			.collect(Collectors.toList());
		return UsuarioDto.builder()
				.id(entity.getId())
				.email(entity.getEmail())
				.activo(entity.isActivo())
				.roles(rolesDto)
				.build();
	}

	@Override
	public Usuario fromDTO(UsuarioDto dto) {
		if(dto==null) return null;
		return Usuario.builder()
				.id(dto.getId())
				.email(dto.getEmail())
				.activo(dto.isActivo())
				.build();
	}

}
