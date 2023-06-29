package br.com.odontoprime.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;

import br.com.odontoprime.dao.PacienteDAO;
import br.com.odontoprime.entidade.Foto;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.util.MensagemUtil;
import br.com.odontoprime.util.Transactional;

@SuppressWarnings("serial")
public class GaleriaService implements Serializable {
	@Inject
	private PacienteDAO pacienteDAO;
	private List<Foto> fotos = new ArrayList<>();

	public void inserirFotoGaleria(Foto foto, FileUploadEvent fileUploadEvent) {
		
		foto.setImagem(fileUploadEvent.getFile().getContents());
		foto.setContentType(fileUploadEvent.getFile().getContentType());
		fotos.add(foto);
		MensagemUtil.enviarMensagem("Foto adicionada com sucesso!", FacesMessage.SEVERITY_INFO);
	}

	public void salvarGaleriaPaciente(Paciente paciente, Foto foto) {

		try {
			if (paciente != null && paciente.getId() != null && !paciente.getId().toString().equals("")) {

				if (fotos != null) {
					paciente = pacienteDAO.buscarPacienteComFotos(paciente.getId());
					paciente.getFotos().addAll(fotos);
					pacienteDAO.atualizar(paciente);
					MensagemUtil.enviarMensagem("Galeria salva com sucesso!", FacesMessage.SEVERITY_INFO);
				} else {
					MensagemUtil.enviarMensagem("Galeria está vazia. Adicione fotos primeiro para depois salvar!",
							FacesMessage.SEVERITY_WARN);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Paciente buscarPacienteComFotos(Long id) {
		return pacienteDAO.buscarPacienteComFotos(id);
	}

}
