package com.sheryians.major.controller;

import com.sheryians.major.global.GlobalDara;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CartController {

    @Autowired
    private ProductService productService;

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable int id) {
        productService.getProductById(id).ifPresent(GlobalDara::addToCart);
        return "redirect:/shop";
    }

    @GetMapping("/cart")
    public String cartGet(Model model) {
        List<Product> cart = GlobalDara.getCart();
        model.addAttribute("cartCount", cart.size());
        model.addAttribute("total", cart.stream().mapToDouble(Product::getPrice).sum());
        model.addAttribute("cart", cart);
        return "cart";
    }

    @GetMapping("/cart/removeItem/{index}")
    public String cartItemRemove(@PathVariable int index) {
        List<Product> cart = GlobalDara.getCart();
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
        return "redirect:/cart";
    }

    @GetMapping("/checkout")
    public String checkout(Model model) {
        List<Product> cart = GlobalDara.getCart();
        model.addAttribute("total", cart.stream().mapToDouble(Product::getPrice).sum());
        return "checkout";
    }
}
