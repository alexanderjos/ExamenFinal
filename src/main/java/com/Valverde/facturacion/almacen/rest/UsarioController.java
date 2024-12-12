package com.Valverde.facturacion.almacen.rest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;

import com.Valverde.facturacion.almacen.converter.UsuarioConverter;
import com.Valverde.facturacion.almacen.dto.UsuarioDto;
import com.Valverde.facturacion.almacen.entity.Usuario;
import com.Valverde.facturacion.almacen.service.PdfService;
import com.Valverde.facturacion.almacen.service.UsuarioService;
import com.Valverde.facturacion.almacen.util.WrapperResponse;

@RestController
@RequestMapping("/v1/usuarios")
//localhost:8090/v1/categorias versionado en la URI
public class UsarioController {
	@Autowired
	private UsuarioService service;
	
	@Autowired
	private UsuarioConverter converter;
	@Autowired
	private PdfService pdfService;
	@GetMapping
	public ResponseEntity<List<UsuarioDto>> findAll(){
		List<UsuarioDto> usuarios = converter.fromEntity(service.findAll());
		return new WrapperResponse(true, "success", usuarios).createResponse(HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<UsuarioDto> create (@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UsuarioDto> update(@PathVariable("id") int id,@RequestBody Usuario usuario){
		UsuarioDto registro = converter.fromEntity(service.save(usuario));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UsuarioDto> findById(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.findById(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	
	//Activar o desactivar un usuario
	@PutMapping("/{id}/activate")
	public ResponseEntity<UsuarioDto> activar(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.activate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}

	@PutMapping("/{id}/deactivate")
	public ResponseEntity<UsuarioDto> desactivar(@PathVariable("id") int id){
		UsuarioDto registro=converter.fromEntity(service.deactivate(id));
		return new WrapperResponse(true, "success", registro).createResponse(HttpStatus.OK);
	}
	@GetMapping("/report")
	public ResponseEntity<byte[]> generarReport(){
		List<UsuarioDto> usuarios = converter.fromEntity(service.findAll());
		LocalDateTime fecha = LocalDateTime.now();
		DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String fechaFormato = fecha.format(formato);

		//Crear el contexto de Thymeleaf con los datos
		Context context = new Context();
		context.setVariable("registros", usuarios);
		context.setVariable("fecha", fechaFormato);

		//Llamar al servicio de PDF para crear el PDF
		byte[] pdfBytes = pdfService.createPdf("UsuarioReport", context);

		//Configuramos los encabezados de la respuesta
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("inline", "reporte-usuarios.pdf");

		return ResponseEntity.ok().headers(headers).body(pdfBytes);

	}
	
}
