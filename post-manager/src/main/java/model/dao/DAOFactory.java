package model.dao;

import java.util.HashMap;
import java.util.Map;

public class DAOFactory {
	
	private static Map<Class<?>, Object> listDAOsInterfaces = new HashMap<Class<?>, Object>();
	
	// Para o DAOFactory funcionar para suas classes de dominio, adicione na 
	// lista suas interfaces e classes DAO na listDAOsInterfaces
	// Se tiver curiosidade, pergunte ao professor sobre o funcionamento de blocos estaticos
	static {
		listDAOsInterfaces.put(PostDAO.class, new MySQLPostDAO());
		listDAOsInterfaces.put(UserDAO.class, new MySQLUserDAO());
		listDAOsInterfaces.put(CompanyDAO.class, new MySQLCompanyDAO());
		listDAOsInterfaces.put(VendaDAO.class, new MySQLVendaDAO());
	}

	@SuppressWarnings("unchecked")
	public static <DAOInterface> DAOInterface createDAO(Class<?> entity){
		return (DAOInterface) listDAOsInterfaces.get(entity);
	} 
}
