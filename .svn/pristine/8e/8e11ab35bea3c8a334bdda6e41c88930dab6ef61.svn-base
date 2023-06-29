package br.com.odontoprime.util;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class MensagemUtil {
	public static void enviarMensagem(String msg, Severity tipo) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(tipo, msg, null));

	}

	public static void enviarMensagem(String id, String msg, Severity tipo) {
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(id, new FacesMessage(tipo, msg, null));

	}
}
