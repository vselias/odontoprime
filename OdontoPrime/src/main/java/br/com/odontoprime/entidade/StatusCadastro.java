package br.com.odontoprime.entidade;

public enum StatusCadastro {
	ATUALIZACAO("Atualização"), CADASTRO("Cadastro");

	private String descricao;

	private StatusCadastro(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
