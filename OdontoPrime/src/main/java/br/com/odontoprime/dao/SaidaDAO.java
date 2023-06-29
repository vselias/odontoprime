package br.com.odontoprime.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.odontoprime.entidade.Saida;
import br.com.odontoprime.util.Transactional;

public class SaidaDAO extends GenericDAO<Saida, Long> implements Serializable {
	private static final long serialVersionUID = -8956669898682143741L;

	@Inject
	private EntityManager entityManager;

	@Transactional
	public Double buscarValorTotal() {
		Double resultado = null;
		try {
			resultado = (Double) entityManager.createQuery("select sum(s.valor) from Saida s").getSingleResult();
			if (resultado != null) {
				return resultado;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new Double(0);
	}

	@Transactional
	public Double buscarSaidaFechamento() {
		Double resultado = null;
		try {
			resultado = (Double) entityManager
					.createQuery("select sum(s.valor) from Saida s where date(s.dataSaida) = CURRENT_DATE")
					.getSingleResult();
			if (resultado == null) {
				resultado = new Double(0);
			}
		} catch (Exception e) {
		}
		return resultado;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	public List<Saida> buscarSaidasFechamento(Date dataSaida) {
		return entityManager.createQuery("select s from Saida s where date(s.dataSaida) = :data")
				.setParameter("data", dataSaida).getResultList();
	}

	@Transactional
	public Double buscarSaidaFechamento(Date data) {
		Double resultado = null;
		try {
			resultado = (Double) entityManager
					.createQuery("select sum(s.valor) from Saida s where date(s.dataSaida) = :data")
					.setParameter("data", data, TemporalType.DATE).getSingleResult();
			if (resultado != null) {
				return resultado;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Double(0);
	}

	@Transactional
	public double buscarValorTotalUltimaSaida(Date data) {
		try {

			Query query = entityManager.createQuery("select sum(s.valor) from Saida s where date(s.dataSaida) = :data");
			query.setParameter("data", data, TemporalType.DATE);
			return (double) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

}
