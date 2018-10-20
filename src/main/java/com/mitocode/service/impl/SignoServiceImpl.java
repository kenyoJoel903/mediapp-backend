package com.mitocode.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.mitocode.dao.ISignoDAO;
import com.mitocode.model.Signo;
import com.mitocode.service.ISignoService;

@Service
public class SignoServiceImpl implements ISignoService {
	
	@Autowired
	private ISignoDAO dao;

	@Override
	public Signo registrar(Signo t) {
		return dao.save(t);
	}

	@Override
	public Signo modificar(Signo t) {
		return dao.save(t);
	}

	@Override
	public void eliminar(int id) {
		dao.delete(id);
	}

	@Override
	public Signo listarId(int id) {
		return dao.findOne(id);
	}

	@Override
	public List<Signo> listar() {
		return dao.findAll();
	}

	@Override
	public Page<Signo> listarPageable(Pageable pageable) {
		return dao.findAll(pageable);
	}

}
