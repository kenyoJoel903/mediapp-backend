package com.mitocode.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Entity
@Table(name = "signo")
public class Signo {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@JsonSerialize(using = ToStringSerializer.class)
	private LocalDateTime fecha;
	
	@Column(precision = 5, scale = 2)
	private double temperatura;
	
	private String pulso;
	
	private String ritmoRespitatorio;
	
	
	@ManyToOne
	@JoinColumn(name = "id_paciente", nullable = false)
	private Paciente paciente;


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public LocalDateTime getFecha() {
		return fecha;
	}


	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}


	public double getTemperatura() {
		return temperatura;
	}


	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}


	public String getPulso() {
		return pulso;
	}


	public void setPulso(String pulso) {
		this.pulso = pulso;
	}


	public String getRitmoRespitatorio() {
		return ritmoRespitatorio;
	}


	public void setRitmoRespitatorio(String ritmoRespitatorio) {
		this.ritmoRespitatorio = ritmoRespitatorio;
	}


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

}
