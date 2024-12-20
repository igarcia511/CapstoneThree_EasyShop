package org.yearup.data.mysql;

import org.springframework.stereotype.Component;
import org.yearup.models.Profile;
import org.yearup.data.ProfileDao;
import org.yearup.models.User;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class MySqlProfileDao extends MySqlDaoBase implements ProfileDao
{
    public MySqlProfileDao(DataSource dataSource)
    {
        super(dataSource);
    }



    @Override
    public Profile getProfile(int usersId){

        String sql = "SELECT u.user_id, p.first_name, p.last_name, p.phone, p.email, p.address, p.city, p.state, p.zip " +
                "FROM users u " +
                "JOIN profiles p ON u.user_id = p.user_id " +
                "WHERE u.user_id = ?;";
        try(Connection connection = getConnection();
        )
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, usersId);
            ResultSet rows = statement.executeQuery();

            if(rows.next()){
                int userId = rows.getInt("user_id");
                String firstName = rows.getString("first_name");
                String lastName = rows.getString("last_name");
                String phone = rows.getString("phone");
                String email = rows.getString("email");
                String address = rows.getString("address");
                String city = rows.getString("city");
                String state = rows.getString("state");
                String zip = rows.getString("zip");
               return new Profile(userId, firstName, lastName, phone, email, address, city, state, zip);

            }


        }
        catch(SQLException e){
            e.printStackTrace();
        }
return null;
    }

    @Override
    public Profile updateProfile(Profile profile, int userId) {
        //user_id INT NOT NULL,
        //    first_name VARCHAR(50) NOT NULL,
        //    last_name VARCHAR(50) NOT NULL,
        //    phone VARCHAR(20) NOT NULL,
        //    email VARCHAR(200) NOT NULL,
        //    address VARCHAR(200) NOT NULL,
        //    city VARCHAR(50) NOT NULL,
        //    state VARCHAR(50) NOT NULL,
        //    zip VARCHAR(20) NOT NULL,
        String sql = "UPDATE profiles SET first_name=?, last_name=?, phone=?, email=?, address=?, city=?," +
                "state=?, zip=? WHERE user_id=?;";

        try(Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, profile.getFirstName());
            statement.setString(2, profile.getLastName());
            statement.setString(3, profile.getPhone());
            statement.setString(4, profile.getEmail());
            statement.setString(5, profile.getAddress());
            statement.setString(6, profile.getCity());
            statement.setString(7, profile.getState());
            statement.setString(8, profile.getZip());

            statement.setInt(9, userId);

            statement.executeUpdate();

            return profile;

        }catch(SQLException e){

            e.printStackTrace();
        }
        return null;
    }


    @Override
    public Profile create(Profile profile)
    {
        String sql = "INSERT INTO profiles (user_id, first_name, last_name, phone, email, address, city, state, zip) " +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = getConnection())
        {
            PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, profile.getUserId());
            ps.setString(2, profile.getFirstName());
            ps.setString(3, profile.getLastName());
            ps.setString(4, profile.getPhone());
            ps.setString(5, profile.getEmail());
            ps.setString(6, profile.getAddress());
            ps.setString(7, profile.getCity());
            ps.setString(8, profile.getState());
            ps.setString(9, profile.getZip());

            ps.executeUpdate();

            return profile;
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }



}
