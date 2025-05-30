package com.sheryians.major.controller;

import com.sheryians.major.dto.ProductDTO;
import com.sheryians.major.model.Category;
import com.sheryians.major.model.Order;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CategoryService;
import com.sheryians.major.service.OrderService;
import com.sheryians.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
public class AdminController {

    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    // Admin Home
    @GetMapping("/admin")
    public ModelAndView adminHome() {
        ModelAndView mav = new ModelAndView("adminHome");
        return mav;
    }

    // --- Category section ---

    // Fetch categories as JSON (REST API)
    @GetMapping("/api/admin/categories")
    public ResponseEntity<List<Category>> getCategoriesAsJson() {
        List<Category> categories = categoryService.getAllCategory();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Get categories view (Admin Page)
    @GetMapping("/admin/categories")
    public ModelAndView getCategoriesView() {
        List<Category> categories = categoryService.getAllCategory();
        ModelAndView mav = new ModelAndView("categories");
        mav.addObject("categories", categories);
        return mav;
    }



    // Add a new category (REST API)
    @PostMapping("/api/admin/categories/add")
    public ResponseEntity<Category> postCategoryAdd(@RequestBody Category category) {
        categoryService.addCategory(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    // Get category form for adding (Admin Page)
    @GetMapping("/admin/categories/add")
    public ModelAndView getCategoryAddForm() {
        ModelAndView mav = new ModelAndView("categoriesAdd");
        mav.addObject("category", new Category());
        return mav;


    }

    // Delete a category by ID (REST API)
    @DeleteMapping("/api/admin/categories/delete/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.removeCategoryById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/admin/categories/update/{id}")
    public ModelAndView getCategoryUpdateForm(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id).orElseThrow(() -> new RuntimeException("Category not found"));
        ModelAndView mav = new ModelAndView("categoriesUpdate");
        mav.addObject("category", category);
        return mav;
    }

    @PostMapping("/admin/categories/update/{id}")
    public ModelAndView updateCategory(@PathVariable int id, @ModelAttribute Category category) {
        categoryService.updateCategory(id, category);
        return new ModelAndView("redirect:/admin/categories");
    }



    // --- Product section ---

    // Fetch all products as JSON (REST API)
    @GetMapping("/api/admin/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProduct();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    // Get products view (Admin Page)
    @GetMapping("/admin/products")
    public ModelAndView getProductsView() {
        List<Product> products = productService.getAllProduct();
        ModelAndView mav = new ModelAndView("products");
        mav.addObject("products", products);
        return mav;
    }



    // Add a new product (REST API)
    @PostMapping("/api/admin/products/add")
    public ModelAndView postProductAdd(
            @RequestParam("name") String name,
            @RequestParam("categoryId") int categoryId,
            @RequestParam("price") double price,
            @RequestParam("weight") double weight,
            @RequestParam("description") String description,
            @RequestParam("productImage") MultipartFile file,
            @RequestParam("imgName") String imgName) throws IOException {

        Product product = new Product();
        product.setName(name);
        product.setCategory(categoryService.getCategoryById(categoryId).orElse(null));
        product.setPrice(price);
        product.setWeight(weight);
        product.setDescription(description);

        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);
        productService.addProduct(product);

        // Redirect to the products view after adding the product
        return new ModelAndView("redirect:/admin/products");
    }


    // Get product form for adding (Admin Page)
    @GetMapping("/admin/products/add")
    public ModelAndView getProductAddForm() {
        ModelAndView mav = new ModelAndView("productsAdd");
        mav.addObject("productDTO", new ProductDTO());
        mav.addObject("categories", categoryService.getAllCategory());
        return mav;
    }

    // Delete a product by ID (REST API)
    @DeleteMapping("/api/admin/products/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable long id) {
        productService.removeProductById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update an existing product (REST API)
    @GetMapping("/admin/product/update/{id}")
    public ModelAndView updateProductGet(@PathVariable long id, Model model) {
        Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Convert Product to ProductDTO
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCategoryId(product.getCategory().getId());
        productDTO.setPrice(product.getPrice());
        productDTO.setWeight(product.getWeight());
        productDTO.setDescription(product.getDescription());
        productDTO.setImageName(product.getImageName());

        // Pass data to the model
        model.addAttribute("categories", categoryService.getAllCategory());
        model.addAttribute("productDTO", productDTO);

        // Return the name of the Thymeleaf template for product update
        return new ModelAndView("productUpdate");
    }



    @PutMapping("/api/admin/products/update/{id}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable long id,
            @ModelAttribute ProductDTO productDTO,
            @RequestParam("productImage") MultipartFile file,
            @RequestParam("imgName") String imgName) throws IOException {

        // Fetch the product
        Product product = productService.getProductById(id).orElseThrow(() -> new RuntimeException("Product not found"));

        // Update product details
        product.setName(productDTO.getName());
        product.setCategory(categoryService.getCategoryById(productDTO.getCategoryId()).orElseThrow());
        product.setPrice(productDTO.getPrice());
        product.setWeight(productDTO.getWeight());
        product.setDescription(productDTO.getDescription());

        // Handle image upload
        String imageUUID;
        if (!file.isEmpty()) {
            imageUUID = file.getOriginalFilename();
            Path fileNameAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNameAndPath, file.getBytes());
        } else {
            imageUUID = imgName;
        }
        product.setImageName(imageUUID);

        // Save updated product
        productService.updateProduct(id, product);

        // Return HTTP 303 (See Other) to redirect to the products page
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header("Location", "/admin/products")
                .build();
    }


    @GetMapping("/admin/orders")
    public ModelAndView getOrdersView() {
        List<Order> orders = orderService.getAllOrders();
        ModelAndView mav = new ModelAndView("orders");
        mav.addObject("orders", orders);
        return mav;
    }

    // Mark an order as complete
    @PutMapping("/api/admin/orders/complete/{id}")
    public ResponseEntity<Void> markOrderComplete(@PathVariable long id) {
        orderService.markOrderComplete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Delete an order
    @DeleteMapping("/admin/orders/delete/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    // Fetch all orders as JSON (REST API)
    @GetMapping("/api/admin/orders")
    public ResponseEntity<List<Order>> getAllOrdersAsJson() {
        List<Order> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    // Fetch a single product by ID (REST API)
    @GetMapping("/api/admin/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable long id) {
        Product product = productService.getProductById(id).orElseThrow();
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

}