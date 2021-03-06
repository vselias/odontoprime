package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.FileUploadEvent;

import br.com.odontoprime.entidade.Foto;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.service.GaleriaService;
import br.com.odontoprime.service.PacienteService;

@Named
@ViewScoped
public class GaleriaMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3957646131909569617L;
	private Paciente paciente;
	private Foto foto;
	private List<Paciente> pacientes;
	@Inject
	private PacienteService pacienteService;
	@Inject
	private GaleriaService galeriaService;
	private List<Paciente> listaPesquisa;
	private String idFoto;

	public List<Paciente> getListaPesquisa() {
		return listaPesquisa;
	}

	public Foto getFoto() {
		return foto;
	}

	public void setFoto(Foto foto) {
		this.foto = foto;
	}

	public Paciente getPaciente() {
		if (paciente == null)
			paciente = new Paciente();
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public List<Paciente> getPacientes() {
		return pacientes;
	}

	@PostConstruct
	public void init() {
		paciente = new Paciente();
		pacientes = pacienteService.buscarTodos();
		foto = new Foto();
		listaPesquisa = new ArrayList<>();
	}

	public void inserirFotoGaleria(FileUploadEvent fileUploadEvent) {
		galeriaService.inserirFotoGaleria(foto, fileUploadEvent);
	}

	public void salvarGaleriaPaciente() {

		galeriaService.salvarGaleriaPaciente(paciente, foto);
		selecionarPaciente(paciente);
	}

	public void atualizarPacienteEFoto() {
		pacientes = null;
		pacientes = pacienteService.buscarTodos();
		foto = new Foto();
	}

	public void selecionarPaciente(Paciente paciente) {
		this.paciente = galeriaService.buscarPacienteComFotos(paciente.getId());
	}

	public void enviarPaciente(Paciente p) {
		this.paciente = p;
	}

	public void selecionarFoto() {
		String id = (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("idFoto");
		idFoto = id;
	}

	public void deletarFoto() {

		System.out.println("Invocou metodo de deletar...! id = " + idFoto);
		galeriaService.deletarFoto(idFoto);
		selecionarPaciente(paciente);
		System.out.println("Id long é: " + idFoto);
		System.out.println("qtd de fotos: " + paciente.getFotos().size());
	}
}
