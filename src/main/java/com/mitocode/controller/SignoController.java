package com.mitocode.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mitocode.exception.ModeloNotFoundException;
import com.mitocode.model.Paciente;
import com.mitocode.model.Signo;
import com.mitocode.service.ISignoService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/signos")
@Api(value = "Servicio REST para los signos")
public class SignoController {
	
	@Autowired
	private ISignoService service;
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Signo>> listar(){
		List<Signo> signos = new ArrayList<>();
		signos = service.listar();
		return new ResponseEntity<>(signos, HttpStatus.OK);
	}
	
	@GetMapping(value="/pageable", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Signo>> listarPageable(Pageable pageable){
		Page<Signo> signos;
		signos = service.listarPageable(pageable);
		return new ResponseEntity<Page<Signo>>(signos, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public Resource<Signo> listarId(@PathVariable("id") Integer id){
		Signo signo = service.listarId(id);
		if(signo == null) {
			throw new ModeloNotFoundException("ID: " + id);
		}
		Resource<Signo> resource = new Resource<Signo>(signo);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarId(id));
		resource.add(linkTo.withRel("paciente-resource"));
		return resource;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Signo signo){
		Signo sig = new Signo();
		sig = service.registrar(signo);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(sig.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@Valid @RequestBody Signo signo){
		service.modificar(signo);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable("id") Integer id) {
		Signo signo = service.listarId(id);
		if(signo == null) {
			throw new ModeloNotFoundException("ID: " + id);
		}else {
			service.eliminar(id);
		}
	}

}
