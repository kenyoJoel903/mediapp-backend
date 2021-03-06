package com.mitocode.service.impl;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mitocode.dao.IConsultaDAO;
import com.mitocode.dao.IConsultaExamenDAO;
import com.mitocode.dto.ConsultaListaExamenDTO;
import com.mitocode.dto.ConsultaResumenDTO;
import com.mitocode.dto.FiltroConsultaDTO;
import com.mitocode.model.Consulta;
import com.mitocode.service.IConsultaService;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Service
public class ConsultaServiceImpl implements IConsultaService{

	@Autowired
	private IConsultaDAO dao;
	
	@Autowired
	private IConsultaExamenDAO ceDAO;
	
	@Override
	public Consulta registrar(Consulta t) {
		return dao.save(t);
	}

	@Override
	public Consulta modificar(Consulta t) {		
		return dao.save(t);
	}

	@Override
	public void eliminar(int id) {
		dao.delete(id);
	}

	@Override
	public Consulta listarId(int id) {
		return dao.findOne(id);
	}

	@Override
	public List<Consulta> listar() {
		return dao.findAll();
	}

	@Transactional
	@Override
	public Consulta registrar(ConsultaListaExamenDTO consultaDTO) {
		consultaDTO.getConsulta().getDetalleConsulta().forEach(x -> x.setConsulta(consultaDTO.getConsulta()));
		dao.save(consultaDTO.getConsulta());
		consultaDTO.getLstExamen().forEach(e -> ceDAO.registrar(consultaDTO.getConsulta().getIdConsulta(), e.getIdExamen()));
		
		return consultaDTO.getConsulta();
	}

	@Override
	public List<Consulta> buscar(FiltroConsultaDTO filtro) {
		return dao.buscar(filtro.getDni(), filtro.getNombreCompleto());
	}

	@Override
	public List<Consulta> buscarfecha(FiltroConsultaDTO filtro) {
		LocalDateTime fechaSgte = filtro.getFechaConsulta().plusDays(1);
		return dao.buscarFecha(filtro.getFechaConsulta(), fechaSgte);
	}

	@Override
	public List<ConsultaResumenDTO> listarResumen() {
		List<ConsultaResumenDTO> consulta = new ArrayList<>();
		
		//List<Object[]>
		//cantidad    fecha
		//[1		,		27/09/2018]
		//[2		,		28/09/2018]
		dao.listarResumen().forEach(x -> {
			ConsultaResumenDTO cr = new ConsultaResumenDTO();
			cr.setCantidad(Integer.parseInt(String.valueOf(x[0])));
			cr.setFecha(String.valueOf(x[1]));
			consulta.add(cr);
		});
		return consulta;
	}

	@Override
	public byte[] generarReporte() {		
		byte[] data = null;
		try {
			File file = new ClassPathResource("/reports/consultas.jasper").getFile();
			JasperPrint print = JasperFillManager.fillReport(file.getPath(), null, new JRBeanCollectionDataSource(this.listarResumen()));
			data = JasperExportManager.exportReportToPdf(print);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return data;	
	}
}
