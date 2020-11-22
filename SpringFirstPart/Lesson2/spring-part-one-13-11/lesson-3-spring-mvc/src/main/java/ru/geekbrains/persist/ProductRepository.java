package ru.geekbrains.persist;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductRepository {

    private final AtomicLong identity = new AtomicLong(0);

    private final Map<Long, Product> identityMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        insert(new Product(null, "MacBook", "Super laptop", new BigDecimal(3000)));
        insert(new Product(null, "iPhone", "Super mobile phone", new BigDecimal(1000)));
        insert(new Product(null, "iPad", "Super tablet PC", new BigDecimal(1500)));
    }

    public void insert(Product product) {
        product.setId(identity.incrementAndGet());
        identityMap.put(product.getId(), product);
    }

    public void update(Product product) {
        identityMap.put(product.getId(), product);
    }

    public void delete(long id) {
        identityMap.remove(id);
    }

    public Product findById(long id) {
        return identityMap.get(id);
    }

    public List<Product> findAll() {
        return new ArrayList<>(identityMap.values());
    }
}
