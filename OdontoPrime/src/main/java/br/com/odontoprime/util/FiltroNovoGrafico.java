package br.com.odontoprime.util;

import java.io.Serializable;

import br.com.odontoprime.entidade.TipoConsulta;

@SuppressWarnings("serial")
public class FiltroNovoGrafico implements Serializable {

	private TipoConsulta primeiroTipoComparacao;
	private TipoConsulta segundoTipoComparacao;
	private int ano;
	private int anoAte;

	public int getAno() {
		return ano;
	}

	public void setAno(int ano) {
		this.ano = ano;
	}

	public int getAnoAte() {
		return anoAte;
	}

	public void setAnoAte(int anoAte) {
		this.anoAte = anoAte;
	}

	public TipoConsulta getPrimeiroTipoComparacao() {
		return primeiroTipoComparacao;
	}

	public void setPrimeiroTipoComparacao(TipoConsulta primeiroTipoComparacao) {
		this.primeiroTipoComparacao = primeiroTipoComparacao;
	}

	public TipoConsulta getSegundoTipoComparacao() {
		return segundoTipoComparacao;
	}

	public void setSegundoTipoComparacao(TipoConsulta segundoTipoComparacao) {
		this.segundoTipoComparacao = segundoTipoComparacao;
	}

}
