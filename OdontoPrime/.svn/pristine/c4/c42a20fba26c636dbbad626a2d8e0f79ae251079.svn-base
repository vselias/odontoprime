package br.odontoprime.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.odontoprime.dao.MovimentacaoCaixaDAO;
import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.entidade.MovimentacaoCaixa;

@FacesConverter(value = "movimentacaoCaixaConverter", forClass = MovimentacaoCaixa.class)
public class MovimentacaoCaixaConverter implements Converter {

	private MovimentacaoCaixaDAO movimentacaoCaixaDAO;
	private MovimentacaoCaixa movimentacaoCaixa = null;

	public MovimentacaoCaixaConverter() {
		this.movimentacaoCaixaDAO = CDILocator.getBean(MovimentacaoCaixaDAO.class);
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {

		if (!"".equals(value) && value != null) {
			movimentacaoCaixa = movimentacaoCaixaDAO.buscarPorId(Long.parseLong(value), MovimentacaoCaixa.class);
			return movimentacaoCaixa;
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		if (value != null) {
			Long codigo = ((MovimentacaoCaixa) value).getId();
			return codigo == null ? "" : codigo.toString();

		}
		return null;
	}

}
