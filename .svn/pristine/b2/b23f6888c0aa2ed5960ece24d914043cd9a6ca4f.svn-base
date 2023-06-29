package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;

import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.service.ConsultaService;

@SuppressWarnings("serial")
@Named
@ViewScoped
public class PesquisaConsultaMB implements Serializable {

	private List<Consulta> consultas;
	@Inject
	private ConsultaService consultaService;

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}

	@PostConstruct
	public void init() {
		consultas = consultaService.buscarTodos();
	}

}
