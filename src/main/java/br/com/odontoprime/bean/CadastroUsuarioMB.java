package br.com.odontoprime.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.cdi.ViewScoped;
import org.primefaces.event.CaptureEvent;

import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.service.ImagemService;
import br.com.odontoprime.service.UsuarioService;
import br.com.odontoprime.util.FacesUtil;
import br.com.odontoprime.util.MensagemUtil;

@Named
@ViewScoped
public class CadastroUsuarioMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3560629135583816066L;
	private Usuario usuario;
	@Inject
	private UsuarioService usuarioService;
	private List<Usuario> usuarios;
	@Inject
	private ImagemService imagemService;
	private boolean exibirWebCam;

	@PostConstruct
	public void initi() {
		usuario = new Usuario();
		exibirWebCam = Boolean.FALSE;
		usuarios = usuarioService.buscarTodos();
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public Usuario getUsuario() {
		if (usuario == null) {
			usuario = new Usuario();
		}
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void salvar() {
		if (isUsuarioCadastravel())
			usuarioService.salvar(usuario);

		resetarObjetos();
	}

	public void atualizarListaUsuarios() {

		usuarios = usuarioService.buscarTodos();
	}

	public void limparCampos() {
		initi();
		MensagemUtil.enviarMensagem("Campos limpos!", FacesMessage.SEVERITY_INFO);
	}

	public boolean isUsuarioCadastravel() {
		return usuarioService.isUsuarioCadastravel(usuario);
	}

	public boolean isExibirWebCam() {
		return exibirWebCam;
	}

	public void setExibirWebCam(boolean exibirWebCam) {
		this.exibirWebCam = exibirWebCam;
	}

	public void ativarWebCam() {
		exibirWebCam = Boolean.TRUE;
	}

	public void resetarObjetos() {
		usuario = new Usuario();
		usuarios = usuarioService.buscarTodos();
	}

	public void tirarFoto(CaptureEvent captureEvent) {
		imagemService.tirarFoto(captureEvent.getData(), usuario);
	}
}
