package br.com.odontoprime.util;

import javax.faces.context.FacesContext;

public class JsfUtil {

	public static boolean isPostBack() {
		return FacesContext.getCurrentInstance().isPostback();
	}

	public static boolean notPostBack() {
		return !isPostBack();

	}

}
