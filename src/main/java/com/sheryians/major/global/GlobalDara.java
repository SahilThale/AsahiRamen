package com.sheryians.major.global;

import com.sheryians.major.model.Product;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GlobalDara {

    // Thread-safe cart list
    private static final List<Product> cart = Collections.synchronizedList(new ArrayList<>());

    /** Get the cart list */
    public static List<Product> getCart() {
        return cart;
    }

    /** Clear the cart */
    public static void clearCart() {
        synchronized (cart) {
            cart.clear();
        }
    }

    /** Add a product to the cart */
    public static void addToCart(Product product) {
        if (product != null) {
            synchronized (cart) {
                cart.add(product);
            }
        }
    }

    /** Remove a product from the cart */
    public static void removeFromCart(Product product) {
        if (product != null) {
            synchronized (cart) {
                cart.remove(product);
            }
        }
    }

    /** Get total number of items in the cart */
    public static int getCartSize() {
        synchronized (cart) {
            return cart.size();
        }
    }

    /** Check if the cart contains a specific product */
    public static boolean containsProduct(Product product) {
        synchronized (cart) {
            return cart.contains(product);
        }
    }
}
