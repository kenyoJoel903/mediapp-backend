package com.mitocode.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.mitocode.model.Especialidad;
import com.mitocode.service.IEspecialidadService;

@RestController
@RequestMapping("/especialidades")
public class EspecialidadController {

	@Autowired
	private IEspecialidadService service;
		
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Especialidad>> listar(){
		List<Especialidad> especialidad = new ArrayList<>();
		especialidad = service.listar();
		return new ResponseEntity<List<Especialidad>>(especialidad, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public Resource<Especialidad> listarId(@PathVariable("id") Integer id) {
		Especialidad esp = service.listarId(id);
		if (esp == null) {
			throw new ModeloNotFoundException("ID: " + id);
		}
		
		Resource<Especialidad> resource = new Resource<Especialidad>(esp);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarId(id));
		resource.add(linkTo.withRel("Especialidad-resource"));
		
		return resource;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Especialidad Especialidad){
		Especialidad esp = new Especialidad();
		esp = service.registrar(Especialidad);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(esp.getIdEspecialidad()).toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@Valid @RequestBody Especialidad Especialidad) {		
		service.modificar(Especialidad);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Especialidad esp = service.listarId(id);
		if (esp == null) {
			throw new ModeloNotFoundException("ID: " + id);
		} else {
			service.eliminar(id);
		}
	}

}
