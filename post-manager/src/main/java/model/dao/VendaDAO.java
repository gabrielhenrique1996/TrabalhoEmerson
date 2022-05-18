package model.dao;

import java.util.List;

import model.ModelException;
import model.Venda;

public interface VendaDAO {
	boolean save(Venda venda) throws ModelException;
	boolean update(Venda venda) throws ModelException;
	boolean delete(Venda venda) throws ModelException;
	List<Venda> listAll() throws ModelException;
	Venda findById(int id) throws ModelException;
}
