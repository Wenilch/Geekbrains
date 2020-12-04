package ru.geekbrains.hibernate.homework;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Random;

public class Application {

	private static EntityManagerFactory emFactory;
	private static final Random random = new Random();

	public static void main(String[] args) {

		emFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

		insertCustomers();
		insertProducts();
		insertOrders();

		EntityManager em = emFactory.createEntityManager();

		List<Product> products = getCustomerProducts(em, "Semen");
		products.forEach(System.out::println);

		List<Customer> customers = getProductCustomers(em, "Intel");
		customers.forEach(System.out::println);

		removeProduct(em, "Intel");
		em.close();

	}

	private static void insertCustomers() {
		EntityManager em = emFactory.createEntityManager();
		em.getTransaction().begin();

		em.persist(new Customer("John"));
		em.persist(new Customer("Semen"));
		em.persist(new Customer("Customer"));
		em.persist(new Customer("Vladimir"));

		em.getTransaction().commit();

		em.close();
	}

	private static void insertProducts() {
		EntityManager em = emFactory.createEntityManager();

		em.getTransaction().begin();

		em.persist(new Product("Intel", 3453453454L));
		em.persist(new Product("AMD", 54444L));
		em.persist(new Product("GeForce", 10000000L));
		em.getTransaction().commit();

		em.close();
	}

	private static void insertOrders() {
		EntityManager em = emFactory.createEntityManager();

		Product intel = getProductByName(em, "Intel");
		Product amd = getProductByName(em, "AMD");
		Product geForce = getProductByName(em, "GeForce");

		Customer john = getCustomerByName(em, "John");
		Customer semen = getCustomerByName(em, "Semen");
		Customer customer = getCustomerByName(em, "Customer");
		Customer vladimir = getCustomerByName(em, "Vladimir");

		addOrder(em, john, amd, intel);
		addOrder(em, semen, amd);
		addOrder(em, customer, geForce, intel);
		addOrder(em, vladimir, amd, geForce);

		em.close();
	}

	private static Product getProductByName(EntityManager em, String name) {
		return em.createNamedQuery("productByName", Product.class)
				.setParameter("name", name)
				.getSingleResult();
	}

	private static Customer getCustomerByName(EntityManager em, String name) {
		return em.createNamedQuery("customerByName", Customer.class)
				.setParameter("name", name)
				.getSingleResult();
	}

	private static void addOrder(EntityManager em, Customer customer, Product... products) {
		em.getTransaction().begin();
		Order order = new Order(null, customer);
		em.persist(order);
		for (Product product : products) {
			em.persist(new OrderValue(product, order, product.getCoast(), random.nextFloat()));
		}
		em.getTransaction().commit();
	}

	private static List<Product> getCustomerProducts(EntityManager em, String customerName) {
		return em.createQuery("select distinct p\n" +
				"          from Customer c\n" +
				"            inner join c.orders as o\n" +
				"            inner join o.items as i\n" +
				"            inner join i.product as p\n" +
				"         where c.name = :name", Product.class)
				.setParameter("name", customerName)
				.getResultList();
	}

	private static List<Customer> getProductCustomers(EntityManager em, String customerName) {
		return em.createQuery("select distinct c\n" +
				"          from Customer c\n" +
				"            inner join c.orders as o\n" +
				"            inner join o.items as i\n" +
				"            inner join i.product as p\n" +
				"         where p.name = :name", Customer.class)
				.setParameter("name", customerName)
				.getResultList();
	}

	private static void removeProduct(EntityManager em, String productName) {
		em.getTransaction().begin();
		Product product = em.createNamedQuery("productByName", Product.class)
				.setParameter("name", productName)
				.getSingleResult();
		em.remove(product);
		em.getTransaction().commit();
	}
}
