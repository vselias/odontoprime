package br.com.odontoprime.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
public class Usuario implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1921845685391042478L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Column(unique = true)
	private String login;
	private String senha;
	@Column(nullable = false, columnDefinition = "TINYINT(1)")
	private boolean adm;
	private String cpf;
	private String email;
	@Transient
	private byte[] byteFoto;
	@Transient
	private String nomeImagemRecortada;
	@Transient
	private String nomeImagemCropper;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataUltimoLogin;
	@Transient
	private Date exibirDataUltimoLogin;

	public Date getExibirDataUltimoLogin() {
		return exibirDataUltimoLogin;
	}

	public void setExibirDataUltimoLogin(Date exibirDataUltimoLogin) {
		this.exibirDataUltimoLogin = exibirDataUltimoLogin;
	}

	public Date getDataUltimoLogin() {
		return dataUltimoLogin;
	}

	public void setDataUltimoLogin(Date dataUltimoLogin) {
		this.dataUltimoLogin = dataUltimoLogin;
	}

	private String nomeImagem;

	public String getNomeImagemCropper() {
		return nomeImagemCropper;
	}

	public void setNomeImagemCropper(String nomeImagemCropper) {
		this.nomeImagemCropper = nomeImagemCropper;
	}

	public String getNomeImagemRecortada() {
		return nomeImagemRecortada;
	}

	public void setNomeImagemRecortada(String nomeImagemRecortada) {
		this.nomeImagemRecortada = nomeImagemRecortada;
	}

	public String getNomeImagem() {
		return nomeImagem;
	}

	public void setNomeImagem(String nomeImagem) {
		this.nomeImagem = nomeImagem;
	}

	public byte[] getByteFoto() {
		return byteFoto;
	}

	public void setByteFoto(byte[] byteFoto) {
		this.byteFoto = byteFoto;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean getAdm() {
		return adm;
	}

	public void setAdm(boolean adm) {
		this.adm = adm;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
