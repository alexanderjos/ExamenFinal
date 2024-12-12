package com.Valverde.facturacion.almacen.rest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.context.Context;

import com.Valverde.facturacion.almacen.converter.InfraccionConverter;
import com.Valverde.facturacion.almacen.dto.InfraccionDto;
import com.Valverde.facturacion.almacen.dto.UsuarioDto;
import com.Valverde.facturacion.almacen.entity.Infraccion;
import com.Valverde.facturacion.almacen.service.InfraccionService;
import com.Valverde.facturacion.almacen.service.PdfService;
import com.Valverde.facturacion.almacen.util.WrapperResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.data.domain.Pageable;




@RestController
@RequestMapping("/v1/infracciones")
public class InfraccionController{
@Autowired
    private InfraccionService service;

    @Autowired
    private InfraccionConverter converter;

    @Autowired
	private PdfService pdfService;


    @GetMapping
    public ResponseEntity<List<InfraccionDto>> findAll(
            @RequestParam(value = "offset", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "limit", required = false, defaultValue = "5") int pageSize
    ) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        List<InfraccionDto> infraccion = converter.fromEntity(service.findAll());
        return new WrapperResponse(true, "success", infraccion).createResponse(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InfraccionDto> create (@RequestBody InfraccionDto infraccion) {
        Infraccion entity = converter.fromDTO(infraccion);
        InfraccionDto dto = converter.fromEntity(service.save(entity));//        return ResponseEntity.ok(dto);
        return new WrapperResponse(true, "success", dto).createResponse(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfraccionDto> update (@PathVariable("id") int id, @RequestBody InfraccionDto infraccion) {
        Infraccion entity = converter.fromDTO(infraccion);
        InfraccionDto dto = converter.fromEntity(service.save(entity));
//        return ResponseEntity.ok(dto);
        return new WrapperResponse(true, "success", dto).createResponse(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete (@PathVariable("id") int id) {
        service.delete(id);
//        return ResponseEntity.ok(null);
        return new WrapperResponse(true, "success", null).createResponse(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfraccionDto> findById (@PathVariable("id") int id) {
        InfraccionDto dto = converter.fromEntity(service.findById(id));

//        return ResponseEntity.ok(dto);
        return new WrapperResponse(true, "success", dto).createResponse(HttpStatus.OK);
    }

    @GetMapping("/report")
	public ResponseEntity<byte[]> generarReport(){
		List<InfraccionDto> infraciones = converter.fromEntity(service.findAll());
		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaFormato = fecha.format(formato);

		//Crear el contexto de Thymeleaf con los datos
		Context context = new Context();
		context.setVariable("registros", infraciones);
		context.setVariable("fecha", fechaFormato);

		//Llamar al servicio de PDF para crear el PDF
		byte[] pdfBytes = pdfService.createPdf("infraccionesReport", context);

		//Configuramos los encabezados de la respuesta
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte-infracciones.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);

	}




}
