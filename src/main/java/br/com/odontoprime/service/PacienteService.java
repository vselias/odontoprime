package br.com.odontoprime.service;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageOutputStream;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.servlet.ServletContext;

import org.hibernate.exception.ConstraintViolationException;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.CroppedImage;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.odontoprime.dao.OrcamentoDAO;
import br.com.odontoprime.dao.PacienteDAO;
import br.com.odontoprime.entidade.StatusCadastro;
import br.com.odontoprime.entidade.Paciente;
import br.com.odontoprime.entidade.Usuario;
import br.com.odontoprime.util.FacesUtil;
import br.com.odontoprime.util.MensagemUtil;
import br.com.odontoprime.validator.CpfValidator;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.draw.LineSeparator;

public class PacienteService implements Serializable {

	private static final long serialVersionUID = -7410790862476205106L;

	private ServletContext servletContext;
	@Inject
	private PacienteDAO pacienteDAO;
	private final String NOME_USUARIO;
	private final String CAMINHO_IMG_WINDOWS;
	private final String CAMINHO_IMG_LINUX;
	private final String CAMINHO_IMAGENS_SERVIDOR;
	private final String SISTEMA_OPERACIONAL;
	private String nomeImagem;
	@Inject
	private OrcamentoDAO orcamentoDAO;

	public PacienteService() {
		servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		NOME_USUARIO = System.getProperty("user.name");
		CAMINHO_IMG_WINDOWS = "C:/Users/" + NOME_USUARIO + "/OP/imagens";
		CAMINHO_IMG_LINUX = "/home/" + NOME_USUARIO + "/op/imagens";
		CAMINHO_IMAGENS_SERVIDOR = servletContext.getRealPath("") + File.separator + "temp" + File.separator
				+ "imagens";
		SISTEMA_OPERACIONAL = System.getProperty("os.name");
	}

	public boolean salvar(Paciente paciente) {
		try {

			Usuario usuarioLogado = (Usuario) FacesUtil.getSessionAttribute("usuario");

			if (paciente.isNovo()) {

				if (usuarioLogado != null) {
					paciente.setNomeUsuarioRegistrou(usuarioLogado.getLogin());
				}
				// seta data de cadastro no sistema
				paciente.setDataCadastro(Calendar.getInstance().getTime());

				paciente.setStatusCadastro(StatusCadastro.CADASTRO);

				// salva o paciente
				pacienteDAO.salvar(paciente);

				MensagemUtil.enviarMensagem("Paciente salvo com sucesso!", FacesMessage.SEVERITY_INFO);

				return Boolean.TRUE;
			} else {
				// se não for novo atualiza
				if (usuarioLogado != null) {
					paciente.setNomeUsuarioRegistrou(usuarioLogado.getLogin());
				}
				// seta data de atualização no sistema
				paciente.setDataCadastro(Calendar.getInstance().getTime());

				paciente.setStatusCadastro(StatusCadastro.ATUALIZACAO);

				pacienteDAO.atualizar(paciente);
				MensagemUtil.enviarMensagem("Paciente atualizado com sucesso!", FacesMessage.SEVERITY_INFO);
				return Boolean.TRUE;
			}

		} catch (PersistenceException e) {
			Throwable t = e.getCause();
			e.printStackTrace();
			while ((t != null) && !(t instanceof ConstraintViolationException)) {
				t = t.getCause();
			}
			if (t instanceof ConstraintViolationException) {
				String msg = t.getCause().getMessage();

				if (msg.contains(paciente.getCpf())) {
					MensagemUtil.enviarMensagem("CPF já cadastrado!", FacesMessage.SEVERITY_ERROR);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			MensagemUtil.enviarMensagem(
					"Erro ao salvar paciente.Contate o administrador ou tente novamente mais tarde!",
					FacesMessage.SEVERITY_ERROR);
		}
		return Boolean.FALSE;

	}

	public void remover(Paciente paciente) {
		try {
			orcamentoDAO.removerDependenciaPaciente(paciente.getId());
			pacienteDAO.remover(pacienteDAO.getReference(Paciente.class, paciente.getId()));
			MensagemUtil.enviarMensagem("Paciente removido com sucesso!", FacesMessage.SEVERITY_INFO);

		} catch (Exception e) {
			e.printStackTrace();
			MensagemUtil.enviarMensagem(
					"Erro ao atualizar paciente.Contate o administrador ou tente novamente mais tarde!",
					FacesMessage.SEVERITY_ERROR);
		}

	}

	public List<Paciente> buscarTodos() {
		return pacienteDAO.buscarTodos(Paciente.class);
	}

	public Paciente buscarPorId(Long id) {
		return pacienteDAO.buscarPorId(id, Paciente.class);
	}

	public String gerarNomeImagemAleatoria() {
		return System.currentTimeMillis() + ".png";
	}

	public String gerarImagemSemFormato() {
		return System.currentTimeMillis() + "";
	}

	public boolean existeCpfBd(Paciente paciente) {

		try {
			if (paciente == null)
				return false;

			return pacienteDAO.existeCpfBd(paciente);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	public void gerarRelatorioPDF(Object documento) {

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		try {
			Document pdf = (Document) documento;
			pdf.addCreationDate();
			pdf.setPageSize(PageSize.A4);
			pdf.open();
			String dataAtual = format.format(new Date().getTime());
			Paragraph paragrafo = new Paragraph();
			Image image = Image.getInstance(this.getClass().getResource("/img/logo-login.png"));
			image.scaleToFit(80, 40);
			image.setAlignment(Element.ALIGN_LEFT);
			paragrafo.add(image);
			paragrafo.setAlignment(Element.ALIGN_RIGHT);
			paragrafo.setSpacingAfter(10);
			paragrafo.add(dataAtual);
			pdf.add(paragrafo);
			LineSeparator lineSeparator = new LineSeparator();
			pdf.add(lineSeparator);
			Paragraph paragraph = new Paragraph("Relatório de Pacientes");
			paragraph.setAlignment(Element.ALIGN_CENTER);
			paragraph.setSpacingAfter(10);
			pdf.add(paragraph);

		} catch (DocumentException | IOException e) {
			e.printStackTrace();
		}

	}

	public StreamedContent exibirImg(Paciente paciente) {
		StreamedContent exibImg = null;
		FacesContext context = FacesContext.getCurrentInstance();

		if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
			return new DefaultStreamedContent();
		} else {

			String id = context.getExternalContext().getRequestParameterMap().get("id");
			Long idPac = null;
			if (id != null)
				idPac = Long.parseLong(id);
			if (idPac != null)
				paciente = pacienteDAO.buscarPorId(idPac, Paciente.class);
			if (paciente != null && paciente.getByteImg() != null) {
				exibImg = new DefaultStreamedContent(new ByteArrayInputStream(paciente.getByteImg()));
//				byte img[] = paciente.getByteImg();
//				return DefaultStreamedContent.builder().stream(() -> {
//					return new ByteArrayInputStream(img);
//				}).build();
			}
		}
		return exibImg;
	}

	public StreamedContent exibirFotoPaciente(Paciente paciente) {

		if (paciente != null && paciente.getByteImg() != null) {
			return new DefaultStreamedContent(new ByteArrayInputStream(paciente.getByteImg()), "image/png",
					paciente.getNomeImagem());
//			byte img[] = paciente.getByteImg();
//			return DefaultStreamedContent.builder().stream(() -> {
//				return new ByteArrayInputStream(img);
//			}).build();
		}
		return null;
	}

	public boolean verificaCpf(Paciente paciente) {

		return CpfValidator.validaCPF(CpfValidator.remove(paciente.getCpf()));
	}

	public boolean recortarImagem(Paciente paciente, CroppedImage croppedImage) {
		boolean fotoRecortada = false;
		if (croppedImage == null) {
			return fotoRecortada;
		}
		nomeImagem = gerarNomeImagemAleatoria();
		try {
			fotoRecortada = criarArquivo(CAMINHO_IMAGENS_SERVIDOR, croppedImage.getBytes(), nomeImagem);
			if (fotoRecortada) {
				MensagemUtil.enviarMensagem("Imagem recortada com sucesso.", FacesMessage.SEVERITY_INFO);
				System.out.println("[recortarImagem] imagem recortada com sucesso.");
				paciente.setImagemRecortada(nomeImagem);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[recortarImagem] erro ao recortar imagem.");
		}
		return fotoRecortada;
	}

	public boolean criarArquivo(String caminho, byte[] dados, String nomeImagem) {

		FileImageOutputStream fileImageOutputStream;
		if (dados != null) {

			try {
				fileImageOutputStream = new FileImageOutputStream(new File(caminho, nomeImagem));
				fileImageOutputStream.write(dados, 0, dados.length);
				fileImageOutputStream.close();
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

	// APENAS QUANDO TIRAR FOTO SERÁ SETADA O NOME DA IMAGEM
	public boolean tirarFoto(byte[] dados, Paciente paciente) {
		boolean fotoTirada = false;
		nomeImagem = gerarNomeImagemAleatoria();
		try {

			fotoTirada = criarArquivo(CAMINHO_IMAGENS_SERVIDOR, dados, nomeImagem);
			System.out.println(CAMINHO_IMAGENS_SERVIDOR);
			if (fotoTirada) {
				if (paciente == null) {
					paciente = new Paciente();
				}
				paciente.setImagemCropper(nomeImagem);
				paciente.setByteImg(dados);
				MensagemUtil.enviarMensagem("Foto tirada com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return fotoTirada;
	}

	public void salvarImagemRecortada(CroppedImage croppedImage, Paciente paciente) {
		boolean imagemSalva = false;
		if (croppedImage == null || croppedImage.getBytes() == null) {
			return;
		}
		nomeImagem = gerarNomeImagemAleatoria();
		try {
			if (SISTEMA_OPERACIONAL.toLowerCase().contains("windows")) {

				imagemSalva = criarArquivo(CAMINHO_IMG_WINDOWS, croppedImage.getBytes(), nomeImagem);
			} else {
				imagemSalva = criarArquivo(CAMINHO_IMG_LINUX, croppedImage.getBytes(), nomeImagem);
			}

			if (imagemSalva) {
				paciente.setNomeImagem(nomeImagem);
				System.out.println("[salvarImagemRecotada] imagem recortada salva com sucesso.");
				MensagemUtil.enviarMensagem("Imagem salva com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[salvarImagemRecotada] erro ao salvar imagem recortada.");
		}

	}

	public void salvarImagem(Paciente paciente) {
		boolean imagemSalva = false;
		try {

			if (SISTEMA_OPERACIONAL.toLowerCase().contains("windows")) {
				imagemSalva = criarArquivo(CAMINHO_IMG_WINDOWS, paciente.getByteImg(), paciente.getNomeImagem());
			} else {
				imagemSalva = criarArquivo(CAMINHO_IMG_LINUX, paciente.getByteImg(), paciente.getNomeImagem());
			}

			if (imagemSalva) {
				MensagemUtil.enviarMensagem("Imagem salva com sucesso!", FacesMessage.SEVERITY_INFO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("[salvarImagem] erro ao salvar imagem.");
		}
	}

	public boolean subirImagem(FileUploadEvent event, Paciente paciente) {
		boolean imagemSalva = false;
		nomeImagem = gerarNomeImagemAleatoria();
		try {
			imagemSalva = criarArquivo(CAMINHO_IMAGENS_SERVIDOR, event.getFile().getContents(), nomeImagem);
			if (imagemSalva) {
				MensagemUtil.enviarMensagem("Foto enviada com sucesso!", FacesMessage.SEVERITY_INFO);
				paciente.setImagemCropper(nomeImagem);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean criarImagemRedimensionada(FileUploadEvent event, Paciente paciente) {
		try {
			BufferedImage bufferedImage = ImageIO.read(event.getFile().getInputstream());
			int new_width = 500;
			int new_height = 350;
			BufferedImage new_img = new BufferedImage(new_width, new_height, BufferedImage.TYPE_INT_RGB);
			Graphics2D g = new_img.createGraphics();
			g.drawImage(bufferedImage, 0, 0, new_width, new_height, null);
			String[] content = event.getFile().getContentType().split("/");
			String formato = content[1];
			String nomeImagem = gerarImagemSemFormato() + "." + formato;
			paciente.setNomeImagem(nomeImagem);
			ImageIO.write(new_img, formato, new File(CAMINHO_IMAGENS_SERVIDOR, paciente.getNomeImagem()));
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
