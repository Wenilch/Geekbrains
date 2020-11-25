package ru.geekbrains.persist;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CustomerRepository {
    private final AtomicLong identity = new AtomicLong(0);

    private final Map<Long, Customer> identityMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        insert(new Customer(null, "Bill", "Super laptop"));
        insert(new Customer(null, "Pall", "Super mobile phone"));
        insert(new Customer(null, "Sten", "Super tablet PC"));
    }

    public void insert(Customer customer) {
        customer.setId(identity.incrementAndGet());
        identityMap.put(customer.getId(), customer);
    }

    public void update(Customer customer) {
        identityMap.put(customer.getId(), customer);
    }

    public void delete(long id) {
        identityMap.remove(id);
    }

    public Customer findById(long id) {
        return identityMap.get(id);
    }

    public List<Customer> findAll() {
        return new ArrayList<>(identityMap.values());
    }

    public Customer create() {
        Customer customer = new Customer(null, "NewFirstName", "NewLastName");
        insert(customer);

        return customer;
    }
}
