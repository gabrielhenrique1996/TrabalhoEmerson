package controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Company;
import model.ModelException;
import model.User;
import model.Venda;
import model.dao.VendaDAO;
import model.dao.DAOFactory;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/vendas", "/venda/form", "/venda/delete", "/venda/insert", "/venda/update"})
public class VendaController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/venda/form": {
			CommonsController.listUsers(req);
			req.setAttribute("action", "insert");
			ControllerUtil.forward(req, resp, "/form-venda.jsp");
			break;
		}
		case "/post-manager/venda/update":{
			CommonsController.listUsers(req);
			req.setAttribute("action", "update");
			Venda c = loadVenda(req);
			req.setAttribute("venda", c);
			ControllerUtil.forward(req, resp, "/form-venda.jsp");
			break;
		}
		default:
			listVenda(req);
			
			ControllerUtil.transferSessionMessagesToRequest(req);
			
			ControllerUtil.forward(req, resp, "/vendas.jsp");
		}
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException {

		String action = req.getRequestURI();

		switch (action) {
		case "/post-manager/venda/insert": {
			insertVenda(req, resp);
			break;
		}
		case "/post-manager/venda/update":{
			updateVenda(req, resp);
			break;
		}
		case "/post-manager/venda/delete":{
			deleteVenda(req, resp);
			break;
		}
		default:
			System.out.println("URL inválida " + action);
		}

		ControllerUtil.redirect(resp, req.getContextPath() + "/vendas");
	}

	private void deleteVenda(HttpServletRequest req, HttpServletResponse resp) {
		String vendaIdParameter = req.getParameter("id");
		
		int vendaId = Integer.parseInt(vendaIdParameter);
		
		VendaDAO dao = DAOFactory.createDAO(VendaDAO.class);
		
		try {
			Venda venda = dao.findById(vendaId);
			
			if (venda == null)
				throw new ModelException("Venda não encontrada para deleção.");
			
			if (dao.delete(venda)) {
				ControllerUtil.sucessMessage(req, "Venda '" + venda.getTipo() + "' deletada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Venda '" + venda.getTipo() + "' não pode ser deletada.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private void updateVenda(HttpServletRequest req, HttpServletResponse resp) {
		String vendaTipo = req.getParameter("tipo");
		String vendaSetor = req.getParameter("setor");
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		
		Venda venda = loadVenda(req);
		venda.setTipo(vendaTipo);
		venda.setSetor(vendaSetor);
		
		Date startDate = formatDate(start);
		venda.setStart(startDate);

		Date endDate = formatDate(end);
		venda.setEnd(endDate);
		
		User user = new User(userId);
		venda.setUser(user);
		
		VendaDAO dao = DAOFactory.createDAO(VendaDAO.class);
		
		try {
			if (dao.update(venda)) {
				ControllerUtil.sucessMessage(req, "Venda '" + venda.getTipo() + "' atualizada com sucesso.");
			}
			else {
				ControllerUtil.errorMessage(req, "Venda '" + venda.getTipo() + "' não pode ser atualizada.");
			}				
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}		
	}

	private void insertVenda(HttpServletRequest req, HttpServletResponse resp) {
		String vendaTipo = req.getParameter("tipo");
		String setor = req.getParameter("setor");
		Integer userId = Integer.parseInt(req.getParameter("user"));
		String start = req.getParameter("start");
		String end = req.getParameter("end");
		
		Venda venda = new Venda();
		venda.setTipo(vendaTipo);
		venda.setSetor(setor);
		
		Date startDate = formatDate(start);
		venda.setStart(startDate);
		
		Date endDate = formatDate(end);
		venda.setEnd(endDate);

		User user = new User(userId);
		venda.setUser(user);

		VendaDAO dao = DAOFactory.createDAO(VendaDAO.class);

		try {
			if (dao.save(venda)) {
				ControllerUtil.sucessMessage(req, 
						"Venda '" + venda.getTipo() + "' salva com sucesso.");
			} else {
				ControllerUtil.errorMessage(req, 
						"Venda '" + venda.getTipo() + "' não pode ser salva.");
			}
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
	}

	private Date formatDate(String stringDate) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(stringDate);
		} catch (ParseException pe) {
			return null;
		}
	}

	private void listVenda(HttpServletRequest req) {
		VendaDAO dao = DAOFactory.createDAO(VendaDAO.class);
		
		List<Venda> vendas = null;
		
		try {
			vendas = dao.listAll();
		} catch (ModelException e) {
			
			e.printStackTrace();
		}
		
		if (vendas != null)
			req.setAttribute("vendas", vendas);
	}
	
	private Venda loadVenda(HttpServletRequest req) {
		String vendaIdParameter = req.getParameter("vendaId");
		
		int vendaId = Integer.parseInt(vendaIdParameter);
		
		VendaDAO dao = DAOFactory.createDAO(VendaDAO.class);
		
		try {
			Venda c = dao.findById(vendaId);
			
			if (c == null)
				throw new ModelException("Venda não encontrada para alteração");
			
			return c;
		} catch (ModelException e) {
			// log no servidor
			e.printStackTrace();
			ControllerUtil.errorMessage(req, e.getMessage());
		}
		
		return null;
	}
}
