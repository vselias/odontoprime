package br.com.odontoprime.service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.hibernate.Hibernate;

import br.com.odontoprime.dao.GenericDAO;
import br.com.odontoprime.entidade.Entrada;
import br.com.odontoprime.entidade.Parcela;
import br.com.odontoprime.util.Transactional;

public class ParcelaDAO extends GenericDAO<Parcela, Long> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -293532097871869274L;
	@Inject
	private EntityManager entityManager;

	@Transactional
	public int removerParcelasPorId(Long idEntrada) throws Exception {
		return entityManager.createQuery("delete from Parcela where entrada_id = :id").setParameter("id", idEntrada)
				.executeUpdate();

	}

	@Transactional
	public void efetuarPagamentoParcela(Parcela parcela) throws Exception {
		atualizar(parcela);

	}

	public List<Parcela> buscarParcelasPorId(Long idEntrada) {
		try {
			List<Parcela> lista = entityManager.createQuery("select p from Parcela p where p.entrada.id = :id ")
					.setParameter("id", idEntrada).getResultList();

			Hibernate.initialize(lista);
			return lista;
		} catch (Exception e) {
		}
		return null;
	}
}
