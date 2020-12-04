package ru.geekbrains.hibernate.homework;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_values")
public class OrderValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private Product product;

	@ManyToOne
	@JoinColumn(name = "order_id")
	private Order order;

	@Column
	private float price;

	@Column
	private float count;

	public OrderValue() {

	}

	public OrderValue(Product product, Order order, float price, float count) {
		this.product = product;
		this.order = order;
		this.price = price;
		this.count = count;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getCount() {
		return count;
	}

	public void setCount(float count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "OrderValue{" +
				"id=" + id +
				", product=" + product +
				", order=" + order +
				", price=" + price +
				", count=" + count +
				'}';
	}
}
