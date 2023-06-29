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
	public void removerFoto(Long id) {
		try {

			Query createNativeQuery = entityManager
					.createNativeQuery("delete from paciente_foto f where f.fotos_id = :id");
			createNativeQuery.setParameter("id", id);
			int executeUpdate = createNativeQuery.executeUpdate();

			if (executeUpdate > 0) {
				Query createQuery = entityManager.createQuery("delete from Foto f where f.id = :id");
				createQuery.setParameter("id", id);
				createQuery.executeUpdate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
