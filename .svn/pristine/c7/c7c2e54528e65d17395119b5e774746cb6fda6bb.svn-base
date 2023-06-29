package br.com.odontoprime.dao;

import java.io.Serializable;
import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import br.com.odontoprime.entidade.MovimentacaoCaixa;
import br.com.odontoprime.util.Transactional;

@SuppressWarnings("serial")
public class MovimentacaoCaixaDAO extends GenericDAO<MovimentacaoCaixa, Long> implements Serializable {
	@Inject
	private EntityManager entityManager;

	@Transactional
	public MovimentacaoCaixa existeFechamento(Date data) {
		MovimentacaoCaixa movimentacaoCaixaVO = null;
		try {
			Query query = entityManager.createQuery(
					"select m from MovimentacaoCaixa m where m.tipoMovimentacao = 'FECHADO' and m.data = :data");
			query.setParameter("data", data, TemporalType.DATE);
			movimentacaoCaixaVO = (MovimentacaoCaixa) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (movimentacaoCaixaVO == null) {
			movimentacaoCaixaVO = new MovimentacaoCaixa();
		}
		return movimentacaoCaixaVO;
	}

	@Transactional
	public MovimentacaoCaixa buscarMovimentacaoComUsuario(Long id) {
		try {
			Query query = entityManager
					.createQuery("select m from MovimentacaoCaixa m join fetch m.usuario where m.id = :id");
			query.setParameter("id", id);
			return (MovimentacaoCaixa) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Double buscarValorInicialFechamento(Date date) {
		Double resultado = null;
		try {
			resultado = (Double) entityManager
					.createQuery("select m.valorInicial from MovimentacaoCaixa m where date(m.data) = :data")
					.setParameter("data", date).getSingleResult();
			if (resultado != null)
				return resultado;
		} catch (Exception e) {
		}
		return new Double(0);
	}

	@Transactional
	public boolean fluxoCaixaFechado() {
		MovimentacaoCaixa movimentacaoCaixa = null;
		try {
			movimentacaoCaixa = (MovimentacaoCaixa) entityManager
					.createQuery(
							"select m from MovimentacaoCaixa m where m.tipoMovimentacao = 'FECHADO' and m.data = CURRENT_DATE")
					.getSingleResult();

			if (movimentacaoCaixa != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Transactional
	public boolean existeFechamentoPorData(Date data) {
		try {
			MovimentacaoCaixa movimentacaoCaixa = (MovimentacaoCaixa) entityManager
					.createQuery(
							"select m from MovimentacaoCaixa m where m.tipoMovimentacao = 'FECHADO' and date(m.data) = :data")
					.setParameter("data", data, TemporalType.DATE).getSingleResult();
			if (movimentacaoCaixa != null) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Transactional
	public MovimentacaoCaixa buscarMovimentacaoAbertura() {
		try {
			Query query = entityManager.createQuery(
					"select m from MovimentacaoCaixa m where m.tipoMovimentacao = 'ABERTO' and m.data = CURRENT_DATE");
			return (MovimentacaoCaixa) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
