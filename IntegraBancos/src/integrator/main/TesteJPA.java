package integrator.main;

import integrator.entities.MercadoA;
import integrator.utils.HibernateUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TesteJPA {

	private static String driverOrigem = "";
	private static String urlOrigem = "";
	private static String userOrigem = "";
	private static String passwordOrigem = "";
	private static String tabelaOrigem = "";
	private static String produtoOrigem = "";
	private static String precoOrigem = "";

	public static void main(String[] args) {

		try {
			readConfig();
			origemToDestino();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	//Funcao para ler o Arquivo config.xml
	public static void readConfig() throws ParserConfigurationException,
			SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse("config.xml");
		NodeList config = doc.getElementsByTagName("config");
		Node noConfig = config.item(0);

		NodeList configuracoes = noConfig.getChildNodes();

		for (int i = 0; i < configuracoes.getLength(); i++) {
			Node no = configuracoes.item(i);
			if (no.getNodeType() == Node.ELEMENT_NODE) {
				Element elemento = (Element) no;
				switch (elemento.getTagName()) {
				case "driver":
					driverOrigem = elemento.getTextContent();
					break;
				case "url":
					urlOrigem = elemento.getTextContent();
					break;
				case "usuario":
					userOrigem = elemento.getTextContent();
					break;
				case "senha":
					passwordOrigem = elemento.getTextContent();
					break;
				case "tabela":
					tabelaOrigem = elemento.getTextContent();
					break;
				case "produto":
					produtoOrigem = elemento.getTextContent();
					break;
				case "preco":
					precoOrigem = elemento.getTextContent();
				default:
					break;
				}
			}
		}

	}

	public static void origemToDestino() throws SQLException {

		Connection dbConnectionOrigem = null;

		PreparedStatement psOrigem = null;

		//Adicionado os campos conforme vem do XML
		String selectOrigem = "SELECT " + produtoOrigem + ", " + precoOrigem + "  FROM " + tabelaOrigem;

		try {

			dbConnectionOrigem = getDBConnectionOrigem();
			psOrigem = dbConnectionOrigem.prepareStatement(selectOrigem);

			ResultSet rsOrigem = psOrigem.executeQuery();

			while (rsOrigem.next()) {
				
				//Campos do xml
				String produto = rsOrigem.getString(produtoOrigem);
				String preco = rsOrigem.getString(precoOrigem);

				MercadoA mercadoA = new MercadoA();

				mercadoA.setDescricao(produto);
				BigDecimal precoVenda = new BigDecimal(preco);
				mercadoA.setPreco(precoVenda);

				Session session = HibernateUtil.getSessionFactory()
						.openSession();
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

		} catch (SQLException e) {
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
			dbConnection = DriverManager.getConnection(urlOrigem, userOrigem,
					passwordOrigem);
			return dbConnection;

		} catch (SQLException e) {

			System.out.println(e.getMessage());
		}
		return dbConnection;
	}
}
