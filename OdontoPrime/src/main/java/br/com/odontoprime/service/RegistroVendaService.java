package br.com.odontoprime.service;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.odontoprime.dao.ConsultaDAO;
import br.com.odontoprime.entidade.Consulta;
import br.com.odontoprime.util.MensagemUtil;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.draw.LineSeparator;

@SuppressWarnings("serial")
public class RegistroVendaService implements Serializable {

	@Inject
	private ConsultaDAO consultaDAO;

	public void gerarPdf(Document documento) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Document pdf = (Document) documento;
			pdf.addCreationDate();
			pdf.setPageSize(PageSize.A4);
			pdf.open();
			String dataAtual = format.format(new Date().getTime());
			Paragraph paragrafo = new Paragraph();
			Image image = Image.getInstance(this.getClass().getResource(
					"/img/logo-login.png"));
			image.scaleToFit(80, 40);
			image.setAlignment(Element.ALIGN_LEFT);
			paragrafo.add(image);
			paragrafo.setAlignment(Element.ALIGN_RIGHT);
			paragrafo.setSpacingAfter(10);
			paragrafo.add(dataAtual);
			pdf.add(paragrafo);
			LineSeparator lineSeparator = new LineSeparator();
			pdf.add(lineSeparator);
			Paragraph paragraph = new Paragraph(
					"Relatório de Consultas por Data");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(30);
			pdf.add(paragraph);
		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

	}

	public Double valorTotalVendaPorData(Date dataInicio, Date dataFinal) {

		Double valor = null;
		Double valorTotal = null;

		if (dataInicio != null && dataFinal != null) {
			if (dataInicio.before(dataFinal) || dataInicio.equals(dataFinal)) {
				valor = consultaDAO.valorTotalVendaPorData(dataInicio,
						dataFinal);
				if (valor != null) {
					valorTotal = new Double(valor);
					MensagemUtil.enviarMensagem("Vendas encontradas! Valor R$ "
							+ valorTotal, FacesMessage.SEVERITY_INFO);
					return valorTotal;
				} else {
					FacesMessage msg = new FacesMessage(
							"Nenhuma venda realizada nesse período!");
					FacesContext.getCurrentInstance().addMessage(null, msg);
					valorTotal = null;
					valor = null;
					return valorTotal;
				}

			} else {
				MensagemUtil.enviarMensagem(
						"Data inicial maior que data final!",
						FacesMessage.SEVERITY_ERROR);
				valorTotal = null;
			}

		}
		return null;
	}

	public List<Consulta> buscarConsultasPorData(Date dataInicio, Date dataFinal) {
		return consultaDAO.buscarConsultasPorData(dataInicio, dataFinal);
	}

}
