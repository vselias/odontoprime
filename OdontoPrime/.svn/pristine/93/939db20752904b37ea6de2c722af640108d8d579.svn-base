package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.OrcamentoDAO;
import br.com.odontoprime.entidade.Orcamento;

@FacesConverter(value = "OrcamentoConverter", forClass = Orcamento.class)
public class OrcamentoConverter implements Converter {

	private OrcamentoDAO orcamentoDAO;
	private Orcamento orcamento = null;

	public OrcamentoConverter() {
		this.orcamentoDAO = CDILocator.getBean(OrcamentoDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		if (value != null && !"".equals(value)) {
			orcamento = orcamentoDAO.buscarPorId(Long.parseLong(value), Orcamento.class);
		}
		return orcamento;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {

			Long codigo = ((Orcamento) value).getId();
			return codigo == null ? "" : codigo.toString();
		}
		return null;
	}

}
