package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProductDao;
import org.yearup.data.ProfileDao;
import org.yearup.data.ShoppingCartDao;
import org.yearup.data.UserDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import java.security.Principal;

// convert this class to a REST controller
// only logged in users should have access to these actions
@RestController
@RequestMapping("cart")
@CrossOrigin
public class ShoppingCartController
{
    // a shopping cart requires
    private ShoppingCartDao shoppingCartDao;
    private UserDao userDao;
    private ProductDao productDao;


       @Autowired
       public ShoppingCartController(ShoppingCartDao shoppingCartDao, UserDao userDao, ProductDao productDao){
       this.shoppingCartDao = shoppingCartDao;
       this.userDao = userDao;
       this.productDao  = productDao;
       }


    // each method in this controller requires a Principal object as a parameter
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
   //@ResponseStatus(HttpStatus.UNAUTHORIZED)
    //@ResponseStatus(HttpStatus.OK)
    public ShoppingCart getCart(Principal principal)
    {
        try
        {
            // get the currently logged in username
            String userName = principal.getName();
            // find database user by userId
            User user = userDao.getByUserName(userName);
            int userId = user.getId();

            // use the shoppingcartDao to get all items in the cart and return the cart
            ShoppingCart cart = shoppingCartDao.getByUserId(userId);

            return cart;

        }
        catch(Exception e)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
        }
    }

    // add a POST method to add a product to the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be added
    @PostMapping("/products/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ShoppingCart addProduct(Principal principal, @PathVariable int id ){
           try {
               String userName = principal.getName();
               int userId = userDao.getIdByUsername(userName);
               return shoppingCartDao.addProduct(userId, id);
           } catch(Exception e){
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
           }

    }


    // add a PUT method to update an existing product in the cart - the url should be
    // https://localhost:8080/cart/products/15 (15 is the productId to be updated)
    // the BODY should be a ShoppingCartItem - quantity is the only value that will be updated
    @PutMapping("products/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.OK)
    public ShoppingCart updateProductQuantity(Principal principal, @PathVariable int id, @RequestBody ShoppingCartItem shoppingCartItem){
           try {
               String userName = principal.getName();
               int userId = userDao.getIdByUsername(userName);
               return shoppingCartDao.updateProductQuantity(userId, id, shoppingCartItem);
           } catch(Exception e){
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
           }

    }



    // add a DELETE method to clear all products from the current users cart
    // https://localhost:8080/cart
    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ShoppingCart deleteCart(Principal principal){
           try {
               String userName = principal.getName();
               int userId = userDao.getIdByUsername(userName);
               return shoppingCartDao.deleteCart(userId);
           } catch(Exception e){
               throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Oops... our bad.");
           }
    }

}
