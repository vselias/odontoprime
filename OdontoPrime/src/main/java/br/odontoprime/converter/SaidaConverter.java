package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.SaidaDAO;
import br.com.odontoprime.entidade.Saida;

@FacesConverter(value = "SaidaConverter", forClass = Saida.class)
public class SaidaConverter implements Converter {
	private SaidaDAO saidaDAO;
	Saida saida = null;

	public SaidaConverter() {
		this.saidaDAO = CDILocator.getBean(SaidaDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (!"".equals(value) && value != null) {
			saida = saidaDAO.buscarPorId(Long.parseLong(value), Saida.class);
			return saida;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Saida) value).getId();
			return codigo == null ? "" : codigo.toString();

		}
		return null;
	}

}
