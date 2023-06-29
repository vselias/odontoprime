package br.com.odontoprime.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import br.com.odontoprime.entidade.Usuario;

/**
 * Servlet Filter implementation class ControleAcesso
 */
@WebFilter(servletNames = "ControleAcesso", urlPatterns = "/sistema/*")
public class ControleAcesso implements Filter {

	/**
	 * Default constructor.
	 */
	public ControleAcesso() {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		Usuario usuario = (Usuario) req.getSession().getAttribute("usuario");

		if (usuario != null)
			chain.doFilter(request, response);
		else
			res.sendRedirect(req.getContextPath() + "/Login.xhtml");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

}
