package model.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Venda;
import model.ModelException;
import model.User;

public class MySQLVendaDAO implements VendaDAO {

	@Override
	public boolean save(Venda venda) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlInsert = "INSERT INTO vendas VALUES (DEFAULT, ?, ?, ?, ?, ?);";
		
		db.prepareStatement(sqlInsert);
		
		db.setString(1, venda.getTipo());
		db.setString(2, venda.getSetor());
		db.setDate(3, venda.getStart() == null ? new Date() : venda.getStart());
			
		if (venda.getEnd() == null)
			db.setNullDate(4);
		else db.setDate(4, venda.getEnd());

		db.setInt(5, venda.getUser().getId());
		
		return db.executeUpdate() > 0;	
	}

	@Override
	public boolean update(Venda venda) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlUpdate = "UPDATE vendas "
				+ " SET tipo = ?, "
				+ " setor = ?, "
				+ " start = ?, "
				+ " end = ?, "
				+ " user_id = ? "
				+ " WHERE id = ?; "; 
		
		db.prepareStatement(sqlUpdate);
		
		db.setString(1, venda.getTipo());
		db.setString(2, venda.getSetor());
		
		db.setDate(3, venda.getStart() == null ? new Date() : venda.getStart());
		
		if (venda.getEnd() == null)
			db.setNullDate(4);
		else db.setDate(4, venda.getEnd());
		
		db.setInt(5, venda.getUser().getId());
		db.setInt(6, venda.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public boolean delete(Venda venda) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sqlDelete = " DELETE FROM vendas "
		         + " WHERE id = ?;";

		db.prepareStatement(sqlDelete);		
		db.setInt(1, venda.getId());
		
		return db.executeUpdate() > 0;
	}

	@Override
	public List<Venda> listAll() throws ModelException {
		DBHandler db = new DBHandler();
		
		List<Venda> vendas = new ArrayList<Venda>();
			
		// Declara uma instrução SQL
		String sqlQuery = " SELECT c.id as 'venda_id', c.*, u.* \n"
				+ " FROM vendas c \n"
				+ " INNER JOIN users u \n"
				+ " ON c.user_id = u.id;";
		
		db.createStatement();
	
		db.executeQuery(sqlQuery);

		while (db.next()) {
			User user = new User(db.getInt("user_id"));
			user.setName(db.getString("nome"));
			user.setGender(db.getString("sexo"));
			user.setEmail(db.getString("email"));
			
			Venda venda = new Venda(db.getInt("venda_id"));
			venda.setTipo(db.getString("tipo"));
			venda.setSetor(db.getString("setor"));
			venda.setStart(db.getDate("start"));
			venda.setEnd(db.getDate("end"));
			venda.setUser(user);
			
			vendas.add(venda);
		}
		
		return vendas;
	}

	@Override
	public Venda findById(int id) throws ModelException {
		DBHandler db = new DBHandler();
		
		String sql = "SELECT * FROM vendas WHERE id = ?;";
		
		db.prepareStatement(sql);
		db.setInt(1, id);
		db.executeQuery();
		
		Venda c = null;
		while (db.next()) {
			c = new Venda(id);
			c.setTipo(db.getString("tipo"));
			c.setSetor(db.getString("setor"));
			c.setStart(db.getDate("start"));
			c.setEnd(db.getDate("end"));
			
			UserDAO userDAO = DAOFactory.createDAO(UserDAO.class); 
			User user = userDAO.findById(db.getInt("user_id"));
			c.setUser(user);
			
			break;
		}
		
		return c;
	}
}
