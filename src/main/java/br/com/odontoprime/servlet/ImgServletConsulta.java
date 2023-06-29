// package br.com.odontoprime.servlet;
//
// import java.io.IOException;
// import java.io.OutputStream;
//
// import javax.inject.Inject;
// import javax.persistence.EntityManager;
// import javax.servlet.ServletException;
// import javax.servlet.annotation.WebServlet;
// import javax.servlet.http.HttpServlet;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import br.com.odontoprime.entidade.Consulta;
//
/// **
// * Servlet implementation class ImagemServlet
// */
// @WebServlet("/consulta/*")
// public class ImgServletConsulta extends HttpServlet {
// private static final long serialVersionUID = 1L;
// @Inject
// private EntityManager entityManager;
//
// /**
// * @see HttpServlet#HttpServlet()
// */
// public ImgServletConsulta() {
// super();
// }
//
// /**
// * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
// * response)
// */
// protected void doGet(HttpServletRequest request, HttpServletResponse
// response)
// throws ServletException, IOException {
//
// String id = request.getPathInfo().substring(1);
// System.out.println(id);
// Consulta consulta = null;
// entityManager.getEntityManagerFactory().getCache().evictAll();
// entityManager.clear();
// consulta = entityManager.find(Consulta.class, Long.parseLong(id));
//
// if (consulta != null && consulta.getImgDente() != null) {
//
// response.setContentType(consulta.getTipoImagem());
// OutputStream outputStream = response.getOutputStream();
// outputStream.write(consulta.getImgDente());
// outputStream.close();
// }
// }
//
// /**
// * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
// * response)
// */
// protected void doPost(HttpServletRequest request, HttpServletResponse
// response)
// throws ServletException, IOException {
// }
//
// }
