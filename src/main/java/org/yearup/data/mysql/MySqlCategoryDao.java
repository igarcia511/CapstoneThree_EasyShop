package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;

import javax.sql.DataSource;
import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao {
    public MySqlCategoryDao(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM "
                      + " categories; ";
        try (Connection connection = getConnection()) {

            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rows = statement.executeQuery();

            while (rows.next()) {
                Category category = mapRow(rows);
                categories.add(category);

            }
            return categories;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category getById(int categoryId) {
        String sql = "SELECT * FROM categories " +
                       " WHERE category_id=?;";
        try (Connection connection = getConnection();

        ) {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            try (ResultSet rows = statement.executeQuery()) {

                if (rows.next()) {
                    return mapRow(rows);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Category create(Category category) {
        // INSERT INTO categories (name, description)
        //VALUES  ('Electronics', 'Explore the latest gadgets and electronic devices.')
        String sql = "INSERT INTO categories(name, description) " +
                       " VALUES(?,?);";
        try (Connection connection = getConnection();) {
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());

            int row = statement.executeUpdate();

            if(row > 0){
                try (ResultSet rows = statement.getGeneratedKeys();){
                    if(rows.next()){
                        category.setCategoryId(rows.getInt(1));
                        return category;
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(int categoryId, Category category) {
        // update category
        String sql = "UPDATE categories SET name=? , description=? " +
                                " WHERE category_id=?;";
        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1,category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);

            statement.executeUpdate();

        } catch(SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int categoryId) {
        // delete category
        String sql = "DELETE FROM categories " +
                " WHERE category_id=?;";

        try(Connection connection = getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);
            statement.executeUpdate();

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    private Category mapRow(ResultSet row) throws SQLException {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category() {{
            setCategoryId(categoryId);
            setName(name);
            setDescription(description);
        }};

        return category;
    }

}
