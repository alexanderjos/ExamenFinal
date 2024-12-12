package com.Valverde.facturacion.almacen.dto;

import java.util.Date;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfraccionDto {
    private int id;
    private String dni;
    private Date fecha;
    private String placa;
    private String descripcion;
    private String infracción;
}
