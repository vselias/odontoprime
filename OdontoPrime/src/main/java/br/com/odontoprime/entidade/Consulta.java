package br.com.odontoprime.entidade;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Consulta implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2506625216868058689L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Enumerated(EnumType.STRING)
	private TipoConsulta tipoConsulta;

	private String nomeUsuarioCadastrou;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCadastro;

	@Enumerated(EnumType.STRING)
	private StatusCadastro statusCadastro;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable = false, unique = true)
	private Date dataConsulta;

	private Double valorConsulta;

	private Double valorTotal;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private Entrada entrada;

	@Enumerated(EnumType.STRING)
	private EstadoConsulta estadoConsulta;

	private String tipoImagem;

	@ManyToOne
	private Paciente paciente;

	public Double getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public String getNomeUsuarioCadastrou() {
		return nomeUsuarioCadastrou;
	}

	public void setNomeUsuarioCadastrou(String nomeUsuarioCadastrou) {
		this.nomeUsuarioCadastrou = nomeUsuarioCadastrou;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public StatusCadastro getStatusCadastro() {
		return statusCadastro;
	}

	public void setStatusCadastro(StatusCadastro statusCadastro) {
		this.statusCadastro = statusCadastro;
	}

	public TipoConsulta getTipoConsulta() {
		return tipoConsulta;
	}

	public void setTipoConsulta(TipoConsulta tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}

	public EstadoConsulta getEstadoConsulta() {
		return estadoConsulta;
	}

	public void setEstadoConsulta(EstadoConsulta estadoConsulta) {
		this.estadoConsulta = estadoConsulta;
	}

	public String getTipoImagem() {
		return tipoImagem;
	}

	public void setTipoImagem(String tipoImagem) {
		this.tipoImagem = tipoImagem;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataConsulta() {
		return dataConsulta;
	}

	public void setDataConsulta(Date dataConsulta) {
		this.dataConsulta = dataConsulta;
	}

	public Double getValorConsulta() {
		return valorConsulta;
	}

	public void setValorConsulta(Double valorConsulta) {
		this.valorConsulta = valorConsulta;
	}

	// construtor para metodo
	public Consulta(String nomeImagem, Long id, Double valorTotal, TipoConsulta tipoConsulta, String paciente,
			Date dataConsulta) {
		this.id = id;
		this.tipoConsulta = tipoConsulta;
		this.valorConsulta = valorTotal;
		this.paciente = new Paciente();
		this.paciente.setNome(paciente);
		this.paciente.setNomeImagem(nomeImagem);
		this.dataConsulta = dataConsulta;
	}

	public Consulta() {
		super();
		entrada = new Entrada();
		paciente = new Paciente();
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
		Consulta other = (Consulta) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Entrada getEntrada() {
		return entrada;
	}

	public void setEntrada(Entrada entrada) {
		this.entrada = entrada;
	}

}
