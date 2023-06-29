package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.ConsultaDAO;
import br.com.odontoprime.entidade.Consulta;

@FacesConverter(value = "ConsultaConverter", forClass = Consulta.class)
public class ConsultaConverter implements Converter {

	private ConsultaDAO consultaDAO;
	private Consulta consulta = null;

	public ConsultaConverter() {
		this.consultaDAO = CDILocator.getBean(ConsultaDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (!"".equals(value) && value != null) {
			consulta = consultaDAO.buscarPorId(Long.parseLong(value), Consulta.class);
			return consulta;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((Consulta) value).getId();
			return codigo == null ? "" : codigo.toString();

		}
		return null;
	}

}
