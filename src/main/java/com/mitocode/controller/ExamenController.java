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
import com.mitocode.model.Examen;
import com.mitocode.service.IExamenService;

@RestController
@RequestMapping("/examenes")
public class ExamenController {

	@Autowired
	private IExamenService service;
		
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Examen>> listar(){
		List<Examen> exaicos = new ArrayList<>();
		exaicos = service.listar();
		return new ResponseEntity<List<Examen>>(exaicos, HttpStatus.OK);
	}
	
	@GetMapping(value = "/{id}")
	public Resource<Examen> listarId(@PathVariable("id") Integer id) {
		Examen exa = service.listarId(id);
		if (exa == null) {
			throw new ModeloNotFoundException("ID: " + id);
		}
		
		Resource<Examen> resource = new Resource<Examen>(exa);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarId(id));
		resource.add(linkTo.withRel("Examen-resource"));
		
		return resource;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Examen Examen){
		Examen exa = new Examen();
		exa = service.registrar(Examen);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(exa.getIdExamen()).toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@Valid @RequestBody Examen Examen) {		
		service.modificar(Examen);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Examen exa = service.listarId(id);
		if (exa == null) {
			throw new ModeloNotFoundException("ID: " + id);
		} else {
			service.eliminar(id);
		}
	}

}
