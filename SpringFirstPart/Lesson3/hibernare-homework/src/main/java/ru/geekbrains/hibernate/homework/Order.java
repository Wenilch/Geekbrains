package ru.geekbrains.hibernate.homework;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@OneToMany(mappedBy = "order")
	private List<OrderValue> values;

	public Order() {
	}

	public Order(Long id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<OrderValue> getValues() {
		return values;
	}

	public void setValues(List<OrderValue> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Order{" +
				"id=" + id +
				", customer=" + customer +
				", values=" + values +
				'}';
	}
}
