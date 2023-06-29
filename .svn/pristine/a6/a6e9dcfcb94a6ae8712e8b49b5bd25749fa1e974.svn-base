package br.com.odontoprime.dao;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ProdutorEntityManager {

	private static final EntityManagerFactory FACTORY = Persistence.createEntityManagerFactory("OdontoPrime");

	public ProdutorEntityManager() {
	}

	@Produces
	@RequestScoped
	public EntityManager criaEntityManager() {
		return FACTORY.createEntityManager();
	}

	public void finaliza(@Disposes EntityManager manager) {
		manager.close();
	}
}
