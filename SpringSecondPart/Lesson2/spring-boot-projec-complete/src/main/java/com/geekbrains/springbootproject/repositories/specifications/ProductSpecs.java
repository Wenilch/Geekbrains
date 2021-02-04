package com.geekbrains.springbootproject.repositories.specifications;

import com.geekbrains.springbootproject.entities.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductSpecs {

	public Specification<Product> findAllProducts(Optional<String> word, Optional<Double> min, Optional<Double> max) {
		Specification<Product> spec = Specification.where(null);
		if (word.isPresent()) {
			spec = spec.and(titleContains(word.get()));
		}
		if (min.isPresent()) {
			spec = spec.and(priceGreaterThanOrEq(min.get()));
		}
		if (max.isPresent()) {
			spec = spec.and(priceLesserThanOrEq(max.get()));
		}

		return spec;
	}

	public Specification<Product> titleContains(String word) {
		return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%" + word + "%");
	}

	public Specification<Product> priceGreaterThanOrEq(double value) {
		return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), value);
	}

	public Specification<Product> priceLesserThanOrEq(double value) {
		return (Specification<Product>) (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), value);
	}
}
