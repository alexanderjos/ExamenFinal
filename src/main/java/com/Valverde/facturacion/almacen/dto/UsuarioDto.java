package com.Valverde.facturacion.almacen.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDto {
    private int id;
    private String email;
    private boolean activo;
    private List<RolDto> roles;

}
