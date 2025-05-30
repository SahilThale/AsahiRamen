package com.sheryians.major.controller;

import com.sheryians.major.global.GlobalDara;
import com.sheryians.major.model.Order;
import com.sheryians.major.model.OrderCompleted;
import com.sheryians.major.model.OrderItem;
import com.sheryians.major.model.Product;
import com.sheryians.major.service.CartService;
import com.sheryians.major.service.OrderCompletedService;
import com.sheryians.major.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;
    @Autowired
    private OrderCompletedService orderCompletedService;

    @PostMapping("/orderPlaced")
    public ModelAndView placeOrder(
            @ModelAttribute("firstName") String firstName,
            @ModelAttribute("lastName") String lastName,
            @ModelAttribute("email") String email,
            @ModelAttribute("phone") String phone,
            @ModelAttribute("city") String city,
            @ModelAttribute("addressLine1") String addressLine1,
            @ModelAttribute("addressLine2") String addressLine2,
            @ModelAttribute("postcode") String postcode,
            @ModelAttribute("additionalInfo") String additionalInfo) {

        try {
            // Debug log for input values
            System.out.println("Placing order for: " + firstName + " " + lastName);

            // Create new Order object
            Order order = new Order();
            order.setFirstName(firstName);
            order.setLastName(lastName);
            order.setEmail(email);
            order.setPhone(phone);
            order.setCity(city);
            order.setAddressLine1(addressLine1);
            order.setAddressLine2(addressLine2);
            order.setPostcode(postcode);
            order.setAdditionalInfo(additionalInfo);
            order.setStatus("Pending");  // Default status

            // Fetch cart using the getter method
            List<Product> cart = GlobalDara.getCart();

            // Calculate total amount
            double totalAmount = cart.stream().mapToDouble(Product::getPrice).sum();
            order.setAmount(totalAmount);

            // Create and associate OrderItems with the order
            List<OrderItem> orderItems = cart.stream().map(product -> {
                OrderItem item = new OrderItem();
                item.setProductName(product.getName()); // Ensure product name is set
                item.setQuantity(1);  // Assuming each product in cart is 1 unit
                item.setPrice(product.getPrice());
                item.setOrder(order);
                return item;
            }).collect(Collectors.toList());

            // Set order items to order
            order.setOrderItems(orderItems);

            // Save the order with associated items
            orderService.saveOrder(order);

            // Clear the cart after order placement
            GlobalDara.clearCart(); // ✅ Using the new method

            // Redirect to a confirmation page
            ModelAndView mav = new ModelAndView("orderPlaced");
            mav.addObject("order", order);
            return mav;
        } catch (Exception e) {
            e.printStackTrace(); // Debugging error
            ModelAndView mav = new ModelAndView("errorPage");
            mav.addObject("message", "Error placing order: " + e.getMessage());
            return mav;
        }
    }

    @PostMapping("/admin/orders/delete/{id}")
    public String deleteOrder(@PathVariable long id) {
        orderService.deleteOrder(id);
        return "redirect:/admin/orders";
    }

    @PostMapping("/admin/orders/mark-completed/{id}")
    public String markOrderCompleted(@PathVariable long id) {
        orderService.markOrderComplete(id);
        return "redirect:/admin/orders";
    }


//    @GetMapping("/complete")
//    public String showCompletedOrders(Model model) {
//        List<Order> completedOrders = orderService.getCompletedOrders();
//        model.addAttribute("completedOrders", completedOrders);
//        return "completedOrders";  // Must match the template name exactly!
//    }
//    @GetMapping("/complete")
//    public String showCompletedOrders(Model model) {
//        List<OrderCompleted> completedOrders = orderCompletedService.getAllCompletedOrders();
//        model.addAttribute("completedOrders", completedOrders);
//        return "completedOrders";// ✅ Make sure the Thymeleaf template is named "completed_orders.html"
//    }
@GetMapping("/admin/orders/complete")
public String showCompletedOrders(Model model) {
    List<OrderCompleted> completedOrders = orderService.getAllCompletedOrders();
    model.addAttribute("completedOrders", completedOrders);
    return "completedOrders";  // Ensure you have the correct Thymeleaf template path
}

}
