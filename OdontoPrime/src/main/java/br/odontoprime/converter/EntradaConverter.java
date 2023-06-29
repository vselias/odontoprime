package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.EntradaDAO;
import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.Entrada;

@FacesConverter(value = "EntradaConverter", forClass = Consulta.class)
public class EntradaConverter implements Converter {

	private EntradaDAO entradaDAO;
	private Entrada entrada;

	public EntradaConverter() {
		this.entradaDAO = CDILocator.getBean(EntradaDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (!"".equals(value) && value != null) {
			entrada = entradaDAO.buscarEntradaComParcelas(Long.parseLong(value));
			return entrada;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Entrada) value).getId();
			return codigo == null ? "" : codigo.toString();

		}
		return null;
	}

}
