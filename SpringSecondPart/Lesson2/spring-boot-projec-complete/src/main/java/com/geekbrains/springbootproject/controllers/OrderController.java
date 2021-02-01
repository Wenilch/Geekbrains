package com.geekbrains.springbootproject.controllers;

import com.geekbrains.springbootproject.entities.Order;
import com.geekbrains.springbootproject.entities.User;
import com.geekbrains.springbootproject.services.*;
import com.geekbrains.springbootproject.utils.ShoppingCart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class OrderController {
	private UserService userService;
	private OrderService orderService;
	private DeliveryAddressService deliverAddressService;
	private ShoppingCart shoppingCart;

	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	@Autowired
	public void setDeliverAddressService(DeliveryAddressService deliverAddressService) {
		this.deliverAddressService = deliverAddressService;
	}

	@Autowired
	public void setShoppingCart(ShoppingCart shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	@GetMapping("/order/fill")
	public String orderFill(Model model, Optional<Principal> principal) {
		if (!principal.isPresent()) {
			return "redirect:/login";
		}

		User user = userService.findByUserName(principal.get().getName());
		model.addAttribute("cart", shoppingCart);
		model.addAttribute("deliveryAddresses", deliverAddressService.getUserAddresses(user.getId()));
		return "order-filler";
	}

	@PostMapping("/order/confirm")
	public String orderConfirm(Model model, Optional<Principal> principal, @RequestParam("phoneNumber") Optional<String> phoneNumber,
							   @RequestParam("deliveryAddress") Optional<Long> deliveryAddressId) {
		if (!principal.isPresent()) {
			return "redirect:/login";
		}
		User user = userService.findByUserName(principal.get().getName());
		Order order = orderService.makeOrder(shoppingCart, user);
		if (deliveryAddressId.isPresent()) {
			order.setDeliveryAddress(deliverAddressService.getUserAddressById(deliveryAddressId.get()));
		}
		if (phoneNumber.isPresent()) {
			order.setPhoneNumber(phoneNumber.get());
		}
		order.setDeliveryDate(LocalDateTime.now().plusDays(7));
		order.setDeliveryPrice(0.0);
		order = orderService.saveOrder(order);
		model.addAttribute("order", order);
		return "order-before-purchase";
	}

	@GetMapping("/order/result/{id}")
	public String orderConfirm(Model model, @PathVariable(name = "id") Optional<Long> id, Optional<Principal> principal) {
		if (!principal.isPresent()) {
			return "redirect:/login";
		}
		// todo ждем до оплаты, проверка безопасности и проблема с повторной отправкой письма сделать одноразовый вход
		User user = userService.findByUserName(principal.get().getName());
		if (id.isPresent()) {
			Order confirmedOrder = orderService.findById(id.get());
			if (!user.getId().equals(confirmedOrder.getUser().getId())) {
				return "redirect:/";
			}

			model.addAttribute("order", confirmedOrder);
			return "order-result";
		}

		return "redirect:/";
	}
}
