package br.com.odontoprime.servlet;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.odontoprime.bean.UsuarioMB;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.MensagemUtil;

public class AutorizacaoListner implements PhaseListener {

	/**
	 * 
	 */
	@ManagedProperty(value = "#{usuarioController}")
	private UsuarioMB usuarioController;
	private static final long serialVersionUID = 6901047801688846719L;

	@Override
	public void afterPhase(PhaseEvent arg0) {
		// FacesContext facesContext = arg0.getFacesContext();
		// String pagina = facesContext.getViewRoot().getViewId();
		// HttpServletRequest request = (HttpServletRequest)
		// facesContext.getExternalContext().getRequest();
		// HttpServletResponse response = (HttpServletResponse)
		// facesContext.getExternalContext().getResponse();
		// Usuario usuario = (Usuario)
		// request.getSession().getAttribute("usuario");
		//
		// if (usuario != null) {
		// System.out.println("NOME USUARIO = " + usuario.getLogin());
		//
		// if (usuario.getAdm() == 1 && pagina.endsWith("Saida.xhtml")) {
		// MensagemUtil.enviarMensagem("Teste", FacesMessage.SEVERITY_INFO);
		// try {
		// response.sendRedirect("Paciente.xtml");
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		// }
		// }
		// }
	}

	@Override
	public void beforePhase(PhaseEvent arg0) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

}
