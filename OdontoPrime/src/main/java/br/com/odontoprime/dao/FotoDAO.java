package br.com.odontoprime.dao;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import br.com.odontoprime.entidade.Foto;
import br.com.odontoprime.util.Transactional;

public class FotoDAO extends GenericDAO<Foto, Long> {
	@Inject
	EntityManager entityManager;

	@Transactional
	public void removerDependenciaPacienteFoto(Long idFoto) {
		Query query = entityManager
				.createNativeQuery("delete from paciente_foto p where p.fotos_id = :id");
		query.setParameter("id", idFoto);
		query.executeUpdate();
	}
}
