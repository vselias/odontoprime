package br.com.odontoprime.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.servlet.ServletContext;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;

import br.com.odontoprime.dao.UsuarioDAO;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.MensagemUtil;

@SuppressWarnings("serial")
public class ImagemService implements Serializable {
	@Inject
	private UsuarioDAO usuarioDAO;
	private ServletContext servletContext;
	private final String USUARIO;
	private final String CAMINHO_WINDOWS;
	private final String CAMINHO_LINUX;
	private final String CAMINHO_SERVIDOR;
	private final String SO;
	private String nomeImagem;

	public String getCAMINHO_SERVIDOR() {
		return CAMINHO_SERVIDOR;
	}

	public String gerarNomeImagemAleatoria() {
		return System.currentTimeMillis() + ".png";
	}

	public ImagemService() {
		servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		USUARIO = System.getProperty("user.name");
		CAMINHO_WINDOWS = servletContext.getRealPath("")+File.separator+"imagens"+File.separator;
		CAMINHO_LINUX = "/home/" + USUARIO + "/op/imagens";
		CAMINHO_SERVIDOR = servletContext.getRealPath("") + File.separator + "temp" + File.separator + "imagens"
				+ File.separator;
		SO = System.getProperty("os.name");
	}

	public boolean recortarImagem(Usuario usuario, CroppedImage croppedImage) {
		boolean fotoRecortada = false;
		usuario.setNomeImagemRecortada(gerarNomeImagemAleatoria());

		if (croppedImage == null) {
			return false;
		}

		try {
			fotoRecortada = criarArquivo(CAMINHO_SERVIDOR, croppedImage.getBytes(), usuario.getNomeImagemRecortada());
			if (fotoRecortada) {
				MensagemUtil.enviarMensagem("Imagem recortada com sucesso.", FacesMessage.SEVERITY_INFO);
				System.out.println("[recortarImagem] imagem recortada com sucesso.");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[recortarImagem] erro ao recortar imagem.");
		}

		return false;
	}

	public boolean salvarImagemRecortada(CroppedImage croppedImage, Usuario usuario) {
		boolean imagemSalva = Boolean.FALSE;

		if (croppedImage == null || croppedImage.getBytes() == null) {
			return false;
		}
		usuario.setNomeImagem(gerarNomeImagemAleatoria());

		try {
			if (SO.toLowerCase().contains("windows")) {

				imagemSalva = criarArquivo(CAMINHO_WINDOWS, croppedImage.getBytes(), usuario.getNomeImagem());
			} else {
				imagemSalva = criarArquivo(CAMINHO_LINUX, croppedImage.getBytes(), usuario.getNomeImagem());
			}

			if (imagemSalva) {
				System.out.println("[salvarImagemRecotada] imagem recortada salva com sucesso."+CAMINHO_WINDOWS);
				MensagemUtil.enviarMensagem("Imagem salva com sucesso!", FacesMessage.SEVERITY_INFO);
				usuarioDAO.salvar(usuario);
				return imagemSalva;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[salvarImagemRecotada] erro ao salvar imagem recortada.");
		}

		return imagemSalva;

	}

	public void salvarImagem(Usuario usuario) {
		boolean imagemSalva = false;
		try {
			usuario.setNomeImagem(gerarNomeImagemAleatoria());

			if (SO.toLowerCase().contains("windows")) {
				imagemSalva = criarArquivo(CAMINHO_WINDOWS, usuario.getByteFoto(), usuario.getNomeImagem());
			} else {
				imagemSalva = criarArquivo(CAMINHO_LINUX, usuario.getByteFoto(), usuario.getNomeImagem());
			}

			if (imagemSalva) {
				MensagemUtil.enviarMensagem("Imagem salva com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[salvarImagem] erro ao salvar imagem.");
		}
	}

	public boolean criarArquivo(String caminho, byte[] dados, String nomeImagem) {

		FileImageOutputStream fileImageOutputStream;
		if (dados != null) {
			try {
				File pastaImg = new File(caminho);
				if (!pastaImg.exists()) {
					pastaImg.mkdirs();
				}
				fileImageOutputStream = new FileImageOutputStream(new File(caminho, nomeImagem));
				fileImageOutputStream.write(dados, 0, dados.length);
				fileImageOutputStream.close();
				System.out.println(caminho);
				System.out.println("[criarArquivo] - Arquivo criado com sucesso.");
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				MensagemUtil.enviarMensagem("É necessário tirar a foto para salva-la!", FacesMessage.SEVERITY_ERROR);

			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("[criarArquivo] - Erro ao criar arquivo.");
			}
		}
		return false;
	}

	public boolean tirarFoto(byte[] dados, Usuario usuario) {
		boolean imagemSalva = false;
		try {
			usuario.setNomeImagemCropper(gerarNomeImagemAleatoria());

			imagemSalva = criarArquivo(CAMINHO_SERVIDOR, dados, usuario.getNomeImagemCropper());

			if (imagemSalva) {
				MensagemUtil.enviarMensagem("Foto capturada com sucesso!", FacesMessage.SEVERITY_INFO);
				return imagemSalva;
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[tirarFoto] erro ao capturar imagem.");
		}
		return imagemSalva;
	}

	public boolean enviarFotoServidor(Usuario usuario, byte[] dados) {

		boolean fotoTirada = false;
		nomeImagem = gerarNomeImagemAleatoria();
		try {

			fotoTirada = criarArquivo(CAMINHO_SERVIDOR, dados, nomeImagem);
			System.out.println(CAMINHO_SERVIDOR);
			if (fotoTirada) {
				if (usuario == null) {
					usuario = new Usuario();
				}
				usuario.setNomeImagemCropper(nomeImagem);
				MensagemUtil.enviarMensagem("Foto enviada com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[enviarFotoServidor] erro ao enviar imagem.");
		}
		return fotoTirada;
	}
}
