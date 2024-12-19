package org.yearup.data;

import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;

public interface ShoppingCartDao
{
    ShoppingCart getByUserId(int userId);
    // add additional method signatures here
    ShoppingCart addProduct(int user_id, int id);
    ShoppingCart updateProductQuantity(int userId, int productId, ShoppingCartItem item);
    ShoppingCart deleteCart(int userId);
}
