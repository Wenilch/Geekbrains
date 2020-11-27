package ru.geekbrains.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.geekbrains.persist.Customer;
import ru.geekbrains.persist.CustomerRepository;

@Controller
@RequestMapping("/customer")
public class CustomerController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String indexProductPage(Model model) {
        logger.info("Product page update");
        model.addAttribute("customers", customerRepository.findAll());
        return "customer";
    }

    @GetMapping("/{id}")
    public String editProduct(@PathVariable(value = "id") Long id, Model model) {
        logger.info("Edit product with id {}", id);
        model.addAttribute("customer", customerRepository.findById(id));
        return "customer_form";
    }

    @GetMapping("/new")
    public String newCustomer(Model model) {
        model.addAttribute("customer", customerRepository.create());
        return "customer_form";
    }

    @PostMapping("/update")
    public String updateCustomer(Customer customer) {
        customerRepository.update(customer);
        return "redirect:/customer";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id, Model model) {
        customerRepository.delete(id);
        return "redirect:/customer";
    }

}
