package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/shop/cart")
public class ShoppingCartController {
    private ShoppingCart shoppingCart;

    @Autowired
    public void setShoppingCartService(ShoppingCart shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    @GetMapping
    public String cartPage(Model model) {
        model.addAttribute("cart", shoppingCart);
        return "cart-page";
    }

    @GetMapping("/add/{id}")
    public String addProductToCart(@PathVariable("id") Optional<Long> id, HttpServletRequest httpServletRequest) {
        id.ifPresent(presentId -> shoppingCart.add(presentId));
        String referrer = httpServletRequest.getHeader("referer");
        return "redirect:" + referrer;
    }
}
