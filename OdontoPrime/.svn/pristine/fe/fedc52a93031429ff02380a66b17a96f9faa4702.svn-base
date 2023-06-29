package br.com.odontoprime.service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

import br.com.odontoprime.dao.ConsultaDAO;
import br.com.odontoprime.util.FiltroNovoGrafico;
import br.com.odontoprime.util.MensagemUtil;
import br.com.odontoprime.util.Transactional;

public class GraficoVendaService implements Serializable {

	private static final long serialVersionUID = -376071935287026562L;

	@Inject
	private ConsultaDAO consultaDAO;

	public Double valorTotalVendasPorAno(int ano) {

		return consultaDAO.totalVendasPorAno(ano);
	}

	public double totalVendaPorAno(int ano) {
		Double valor = null;
		if (ano > 0)
			valor = consultaDAO.totalVendasPorAno(ano);
		if (valor == null) {
			valor = new Double(0);
		}
		return valor;
	}

	public BarChartModel preencherBarChartModel(Date primeiraData, Date segundaData) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		BarChartModel barChartModel = new BarChartModel();
		ChartSeries chartPrimeiraData = new ChartSeries();

		ChartSeries chartSegundaData = new ChartSeries();

		String data = "";
		Double valor = 0D;
		if (primeiraData != null) {

			Object[] retornoPrimeiraData = consultaDAO.buscarValorTotalEData(primeiraData);
			data = retornoPrimeiraData[1].toString();
			valor = Double.parseDouble(retornoPrimeiraData[0].toString());

			if (retornoPrimeiraData[1] != null)
				chartPrimeiraData.setLabel(format.format(retornoPrimeiraData[1]));

			if (valor != null) {
				chartPrimeiraData.set(data, valor);
			}

		}

		if (segundaData != null) {
			Object[] retornoSegundaData = consultaDAO.buscarValorTotalEData(segundaData);
			data = retornoSegundaData[1].toString();
			valor = Double.parseDouble(retornoSegundaData[0].toString());

			if (retornoSegundaData[1] != null)
				chartSegundaData.setLabel(format.format(retornoSegundaData[1]));

			if (valor != null) {
				chartSegundaData.set(data, valor);
			}

		}

		barChartModel.addSeries(chartPrimeiraData);
		barChartModel.addSeries(chartSegundaData);

		barChartModel.setTitle("Comparação de vendas por data");
		barChartModel.setLegendPosition("ne");

		Axis xAxis = barChartModel.getAxis(AxisType.X);
		xAxis.setLabel("Mes");

		Axis yAxis = barChartModel.getAxis(AxisType.Y);
		yAxis.setLabel("Valor R$");
		yAxis.setMin(0);

		return barChartModel;
	}

	public BarChartModel novoBarChartModel(FiltroNovoGrafico filtroNovoGrafico) {
		BarChartModel barChartModel = new BarChartModel();

		ChartSeries primeiraComparacao = new ChartSeries();
		ChartSeries segundaComparacao = new ChartSeries();

		barChartModel.setTitle("Comparação de vendas por tipos de consulta");
		barChartModel.setLegendPosition("ne");

		String txtPrimero = "";
		String txtSegundo = "";
		
		if (filtroNovoGrafico != null && filtroNovoGrafico.getPrimeiroTipoComparacao() != null
				&& filtroNovoGrafico.getSegundoTipoComparacao() != null) {

			txtPrimero = filtroNovoGrafico.getPrimeiroTipoComparacao().getDescricao();
			txtSegundo = filtroNovoGrafico.getSegundoTipoComparacao().getDescricao();
		}

		primeiraComparacao.setLabel(txtPrimero);
		segundaComparacao.setLabel(txtSegundo);

		Axis xAxis = barChartModel.getAxis(AxisType.X);

		xAxis.setLabel("Ano");

		Axis yAxis = barChartModel.getAxis(AxisType.Y);
		yAxis.setLabel("Valor R$");

		if (filtroNovoGrafico != null && filtroNovoGrafico.getPrimeiroTipoComparacao() != null
				&& filtroNovoGrafico.getSegundoTipoComparacao() != null && filtroNovoGrafico.getAno() != 0) {
			try {
				Double valorPrimeira = null;
				Double valorSegunda = null;

				if (filtroNovoGrafico.getAnoAte() == 0) {
					valorPrimeira = consultaDAO.vendasPorAnoGrafico(filtroNovoGrafico.getPrimeiroTipoComparacao(),
							filtroNovoGrafico.getAno());
					valorSegunda = consultaDAO.vendasPorAnoGrafico(filtroNovoGrafico.getSegundoTipoComparacao(),
							filtroNovoGrafico.getAno());

					if (valorPrimeira == null) {
						valorPrimeira = new Double(0);
					}

					primeiraComparacao.set(filtroNovoGrafico.getAno(), valorPrimeira);

					if (valorSegunda == null) {
						valorSegunda = new Double(0);
					}
					segundaComparacao.set(filtroNovoGrafico.getAno(), valorSegunda);

					barChartModel.addSeries(primeiraComparacao);
					barChartModel.addSeries(segundaComparacao);
					return barChartModel;

				} else {

					for (int i = filtroNovoGrafico.getAno(); i <= filtroNovoGrafico.getAnoAte(); i++) {

						valorPrimeira = consultaDAO.vendasPorAnoGrafico(filtroNovoGrafico.getPrimeiroTipoComparacao(),
								i);
						valorSegunda = consultaDAO.vendasPorAnoGrafico(filtroNovoGrafico.getSegundoTipoComparacao(), i);

						if (valorPrimeira == null)
							valorPrimeira = new Double(0);

						primeiraComparacao.set(i, valorPrimeira);

						if (valorSegunda == null)
							valorSegunda = new Double(0);

						segundaComparacao.set(i, valorSegunda);
					}
					barChartModel.addSeries(primeiraComparacao);
					barChartModel.addSeries(segundaComparacao);
					return barChartModel;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		MensagemUtil.enviarMensagem("Para comparar as consultas é necessário preencher os dados de comparação!",
				FacesMessage.SEVERITY_ERROR);
		return barChartModel;
	}
}
