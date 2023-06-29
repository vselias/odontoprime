package br.com.odontoprime.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.odontoprime.entidade.Foto;

@SuppressWarnings("serial")
@WebServlet("/galeria/*")
public class ImgServletPaciente extends HttpServlet {
	@Inject
	private EntityManager entityManager;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String id = req.getPathInfo().substring(1);
		System.out.println(id);
		Foto foto = null;
		entityManager.getEntityManagerFactory().getCache().evictAll();
		entityManager.clear();
		foto = entityManager.find(Foto.class, Long.parseLong(id));

		if (foto != null && foto.getImagem() != null) {

			resp.setContentType(foto.getContentType());
			OutputStream outputStream = resp.getOutputStream();
			outputStream.write(foto.getImagem());
			outputStream.close();
		}
	}
}
