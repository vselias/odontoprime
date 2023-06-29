package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.Date;

public class ObjConsulta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7462077870208090131L;
	private String nomeConsulta;
	private Integer ano;
	private Double valorConsulta;
	private String nomePaciente;
	private Date dataConsulta;

	public Date getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Date dataConsullta) {
		this.dataConsulta = dataConsullta;
	}

	public String getNomePaciente() {
		return nomePaciente;
	}

	public void setNomePaciente(String nomePaciente) {
		this.nomePaciente = nomePaciente;
	}

	public String getNomeConsulta() {
		return nomeConsulta;
	}

	public void setNomeConsulta(String nomeConsulta) {
		this.nomeConsulta = nomeConsulta;
	}

	public Integer getAnoConsulta() {
		return ano;
	}

	public void setAnoConsulta(Integer dataConsulta) {
		this.ano = dataConsulta;
	}

	public Double getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(Double valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

}
