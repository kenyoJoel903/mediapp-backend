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
import org.springframework.security.access.prepost.PreAuthorize;
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
import com.mitocode.model.Medico;
import com.mitocode.service.IMedicoService;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private IMedicoService service;

	@PreAuthorize("@restAuthService.hasAccess('listar')")
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Medico>> listar(){
		List<Medico> medicos = new ArrayList<>();
		medicos = service.listar();
		return new ResponseEntity<List<Medico>>(medicos, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	@GetMapping(value = "/{id}")
	public Resource<Medico> listarId(@PathVariable("id") Integer id) {
		Medico med = service.listarId(id);
		if (med == null) {
			throw new ModeloNotFoundException("ID: " + id);
		}
		
		Resource<Medico> resource = new Resource<Medico>(med);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).listarId(id));
		resource.add(linkTo.withRel("Medico-resource"));
		
		return resource;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> registrar(@Valid @RequestBody Medico Medico){
		Medico med = new Medico();
		med = service.registrar(Medico);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(med.getIdMedico()).toUri();
		return ResponseEntity.created(location).build();		
	}
	
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> actualizar(@Valid @RequestBody Medico Medico) {		
		service.modificar(Medico);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public void eliminar(@PathVariable Integer id) {
		Medico med = service.listarId(id);
		if (med == null) {
			throw new ModeloNotFoundException("ID: " + id);
		} else {
			service.eliminar(id);
		}
	}

}
