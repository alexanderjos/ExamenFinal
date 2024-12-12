package com.Valverde.facturacion.almacen.entity;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "infracciones")
@EntityListeners(AuditingEntityListener.class)
public class Infraccion{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 8, nullable = false)
    private String dni;

    @Column(name="fecha")
    private Date fecha;

    @Column(length = 7, nullable = false)
    private String placa;


    @Column(length = 200, nullable = false)
    private String infracción;

    @Column(name = "descripcion", length = 255)
    private String descripcion;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
