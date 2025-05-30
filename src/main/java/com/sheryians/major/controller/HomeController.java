package com.sheryians.major.controller;

import com.sheryians.major.global.GlobalDara;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        model.addAttribute("cartCount", GlobalDara.getCartSize());
        return "index.html";
    }

    @GetMapping("/shop")
    public String shop(Model model) {
        model.addAttribute("cartCount", GlobalDara.getCartSize());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProduct());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String shopByCategory(Model model, @PathVariable int id) {
        model.addAttribute("cartCount", GlobalDara.getCartSize());
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("products", productService.getAllProductsByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProduct(Model model, @PathVariable int id) {
        model.addAttribute("cartCount", GlobalDara.getCartSize());
        model.addAttribute("product", productService.getProductById(id).orElse(null));
        return "viewProduct";
    }

    @GetMapping("/menu")
    public String showMenuPage() {
        return "menu";
    }
}
