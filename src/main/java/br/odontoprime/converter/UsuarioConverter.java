package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.UsuarioDAO;
import br.com.odontoprime.entidade.Usuario;

@FacesConverter(value = "usuarioConverter", forClass = Usuario.class)
public class UsuarioConverter implements Converter {

	private UsuarioDAO usuarioDAO;
	private Usuario usuario = null;

	public UsuarioConverter() {
		this.usuarioDAO = CDILocator.getBean(UsuarioDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && !"".equals(value)) {
			usuario = usuarioDAO.buscarPorId(Long.parseLong(value), Usuario.class);
		}
		return usuario;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {

			Long codigo = ((Usuario) value).getId();
			return codigo == null ? "" : codigo.toString();
		}
		return null;
	}

}
