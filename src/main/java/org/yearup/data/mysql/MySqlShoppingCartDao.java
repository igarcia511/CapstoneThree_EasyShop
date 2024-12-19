package org.yearup.data.mysql;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;
import org.yearup.data.ShoppingCartDao;
import org.yearup.models.Product;
import org.yearup.models.ShoppingCart;
import org.yearup.models.ShoppingCartItem;
import org.yearup.models.User;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class MySqlShoppingCartDao extends MySqlDaoBase implements ShoppingCartDao {



    public MySqlShoppingCartDao(DataSource dataSource){
        super(dataSource);

    }

    @Override
    public ShoppingCart getByUserId(int userId) {
        // change to p.*
        String sql = "SELECT sc.product_id, sc.quantity, p.name, p.price, p.category_id, p.description, p.color, p.stock, p.featured, p.image_url " +
                " FROM shopping_cart sc JOIN products p ON sc.product_id = p.product_id " +
                " WHERE sc.user_id=?;";
        ShoppingCart cart = new ShoppingCart();
              try(Connection connection = getConnection();){
                  PreparedStatement statement = connection.prepareStatement(sql);
                  statement.setInt(1, userId);



                  try(ResultSet rows = statement.executeQuery()){

                      while(rows.next()){
                        int productId = rows.getInt("product_id");
                        String name = rows.getString("name");
                          BigDecimal price = rows.getBigDecimal("price");
                       int categoryId = rows.getInt("category_id");
                       String description = rows.getString("description");
                       String color = rows.getString("color");
                       int stock = rows.getInt("stock");
                       boolean isFeatured = rows.getBoolean("featured");
                       String imgUrl = rows.getString("image_url");
                       Product product = new Product(productId, name, price, categoryId, description, color, stock, isFeatured, imgUrl);
                          ShoppingCartItem shoppingCartItem = new ShoppingCartItem();
                          shoppingCartItem.setProduct(product);
                          shoppingCartItem.setQuantity(rows.getInt("quantity"));

                          cart.add(shoppingCartItem);


                      }
                  }

              } catch(SQLException e){
                  e.printStackTrace();
              }
            return cart;
    }

    @Override
    public ShoppingCart addProduct(int userId, int productId) {
        // INSERT INTO shopping_cart (user_id, product_id, quantity)
        //VALUES  (2, 8, 1),
        //        (2, 10, 1);
        ShoppingCart shoppingCart = getByUserId(userId);

        String sql = shoppingCart.contains(productId) ? "UPDATE shopping_cart SET quantity = quantity + 1 WHERE user_id=? AND product_id=?;" :
                "INSERT INTO shopping_cart(user_id, product_id, quantity) VALUES(?,?,1);";


        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);
            statement.setInt(2, productId);

            statement.executeUpdate();

        } catch(SQLException e ){
            e.printStackTrace();
        }
      return shoppingCart;
    }

    @Override
    public ShoppingCart updateProductQuantity(int userId, int productId, ShoppingCartItem item) {

        String sql = "UPDATE shopping_cart SET quantity = quantity + ? WHERE user_id=? AND product_id=?;";
        ShoppingCart shoppingCart = getByUserId(userId);
        if(shoppingCart.contains(productId)){

        try(Connection connection = getConnection();){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, item.getQuantity());
            statement.setInt(2, userId);
            statement.setInt(3, productId);

            statement.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        } }
return shoppingCart;
    }

    public ShoppingCart deleteCart(int userId){
        String sql = "DELETE FROM shopping_cart WHERE user_id=?;";

        ShoppingCart shoppingCart = getByUserId(userId);

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, userId);

            statement.executeUpdate();


        }catch (SQLException e){
            e.printStackTrace();
        }
        return shoppingCart;

    }


}
