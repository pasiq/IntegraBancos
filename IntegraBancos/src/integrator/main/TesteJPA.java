package integrator.main;

import integrator.entities.MercadoA;
import integrator.utils.HibernateUtil;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;


public class TesteJPA {
	
	private static final String driverOrigem = "com.mysql.jdbc.Driver";
	private static final String urlOrigem = "jdbc:mysql://192.168.25.2:3306/mercado_a";
	private static final String userOrigem = "root";
	private static final String passwordOrigem = "";
	
	
	public static void main(String[] args) {
		
		try {
			
			origemToDestino();
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void origemToDestino() throws SQLException
	{

		Connection dbConnectionOrigem = null;
		
		PreparedStatement psOrigem = null;
		
		String selectOrigem = "SELECT produto, preco FROM PRODUTOS";
		
		try{
			
			dbConnectionOrigem = getDBConnectionOrigem();
			psOrigem = dbConnectionOrigem.prepareStatement(selectOrigem);
			
			
			ResultSet rsOrigem = psOrigem.executeQuery();
			
			while(rsOrigem.next()){
				
				String produto = rsOrigem.getString("produto");
				String preco = rsOrigem.getString("preco");
				
				MercadoA mercadoA = new MercadoA();
				
				mercadoA.setDescricao(produto);
				BigDecimal precoVenda = new BigDecimal(preco);
				mercadoA.setPreco(precoVenda);
				
				Session session = HibernateUtil.getSessionFactory().openSession();
				Transaction transaction = session.beginTransaction();
				session.save(mercadoA);
				transaction.commit();
				session.close();
				
				System.out.println("\n##### BANCO DE DADOS ORIGEM ######");	
				System.out.println("Produto: " + produto);
				System.out.println("Preço: " + preco + "\n");
				
				System.out.println("\n##### BANCO DE DADOS DESTINO #######");
				System.out.println("Produto: " + mercadoA.getDescricao());
				System.out.println("Preço: " + mercadoA.getPreco(precoVenda));
			}
			
		}catch (SQLException e){
			System.out.println(e.getMessage());
		}	
	}
	
	private static Connection getDBConnectionOrigem() {
		
		Connection dbConnection = null;
		
		try {
			
			Class.forName(driverOrigem);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		try {
			dbConnection = DriverManager.getConnection(urlOrigem, userOrigem, passwordOrigem);
			return dbConnection;
			
		} catch(SQLException e){
			
			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}
