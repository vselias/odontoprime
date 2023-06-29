package br.com.odontoprime.dao;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.util.Transactional;

public class PacienteDAO extends GenericDAO<Paciente, Long> implements Serializable {
	@Inject
	EntityManager entityManager;

	private static final long serialVersionUID = 1L;

	@Transactional
	public boolean existeCpfBd(Paciente paciente) {
		Paciente pacienteResultado = null;
		String consulta = "select p from Paciente p where p.cpf = :cpf";
		Query query = entityManager.createQuery(consulta);
		query.setParameter("cpf", paciente.getCpf());
		try {
			pacienteResultado = (Paciente) query.getSingleResult();
			if (pacienteResultado != null)
				if (pacienteResultado.getId() != null && pacienteResultado.getId() > 0)
					return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Transactional
	public Paciente buscarPacienteComFotos(Long id) {
		try {
			return (Paciente) entityManager
					.createQuery("select p from Paciente p left  join fetch p.fotos f where p.id = :id")
					.setParameter("id", id).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Transactional
	public Paciente buscarPacienteComConsultas(Long id) {
		try {
			Paciente paciente = buscarPorId(id, Paciente.class);
			paciente.getConsultas().size();
			return paciente;
		} catch (Exception e) {
		}
		return null;
	}

}
