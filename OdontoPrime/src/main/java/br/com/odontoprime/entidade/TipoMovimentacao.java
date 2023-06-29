package br.com.odontoprime.entidade;

public enum TipoMovimentacao {
	ABERTO("Aberto"), FECHADO("Fechado");

	private String descricao;

	private TipoMovimentacao(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
