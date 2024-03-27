package com.design_shinbi.tsubuyaki.model.dao;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.design_shinbi.tsubuyaki.model.Const;
import com.design_shinbi.tsubuyaki.model.entity.User;

public class UserDAO {
    protected Connection connection;
    
    public UserDAO(Connection connection) 
    		throws NoSuchAlgorithmException, SQLException {
        this.connection = connection;
        this.init();
    }
    
    private void init() throws SQLException, NoSuchAlgorithmException {
        if(this.count() == 0) {
            this.add(
                Const.DEFAULT_USER_NAME,
                Const.DEFAULT_USER_EMAIL,
                Const.DEFAULT_USER_PASSWORD,
                true
            );
        }
    }

    
    public static String createHash(String password) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        String string = password;

        byte[] bytes = sha256.digest(string.getBytes());
        String hash = String.format(
                "%040x",
                new BigInteger(1, bytes));

        return hash;
    }
    
    public void add(String name, String email, String password, boolean isAdmin) 
            throws NoSuchAlgorithmException, SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String hash = createHash(password);
        
        String sql = "INSERT INTO users (name, email, password, "
                + "is_admin, created_at, updated_at) "
                + "values (?, ?, ?, ?, ?, ?)";
        
        PreparedStatement statement = this.connection.prepareStatement(sql);
        
        statement.setString(1,  name);
        statement.setString(2, email);
        statement.setString(3, hash);
        statement.setBoolean(4,  isAdmin);
        statement.setTimestamp(5,  now);
        statement.setTimestamp(6, now);
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void update(User user) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        
        String sql = "UPDATE users SET name = ?, email = ?, is_admin = ?, "
                + "updated_at = ? WHERE id = ?";
        
        PreparedStatement statement = this.connection.prepareStatement(sql);
        
        statement.setString(1,  user.getName());
        statement.setString(2,  user.getEmail());
        statement.setBoolean(3,  user.isAdmin());
        statement.setTimestamp(4, now);
        statement.setInt(5, user.getId());
        
        statement.executeUpdate();
        statement.close();
    }
    
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        
        PreparedStatement statement = this.connection.prepareStatement(sql);
        
        statement.setInt(1,  id);
        
        statement.executeUpdate();
        statement.close();
    }
    
    private User createUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setAdmin(resultSet.getBoolean("is_admin"));
        user.setCreatedAt(resultSet.getTimestamp("created_at"));
        user.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return user;
    }
    
    public List<User> findAll() throws SQLException {
        List<User> list = new ArrayList<User>();
        
        String sql = "SELECT * FROM users";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            User user = createUser(resultSet);
            list.add(user);
        }
        resultSet.close();
        statement.close();
        
        return list;
    }
    
    public User find(int id) throws SQLException {
        User user = null;

        String sql = "SELECT * FROM users WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            user = createUser(resultSet);
        }
        resultSet.close();
        statement.close();

        return user;
    }
    
    public User findByEmail(String email) throws SQLException {
        User user = null;

        String sql = "SELECT * FROM users WHERE email = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            user = createUser(resultSet);
        }
        resultSet.close();
        statement.close();

        return user;
    }
    
    public int count() throws SQLException {
        int count = 0;
        
        String sql = "SELECT COUNT(*) AS count FROM users";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        if (resultSet.next()) {
            count = resultSet.getInt("count");
        }
        
        resultSet.close();
        statement.close();
        
        return count;
    }
    
    public User login(String email, String password) 
            throws SQLException, NoSuchAlgorithmException{
        User user = this.findByEmail(email);
        
        if (user != null) {
            String hash = createHash(password);
            if (!user.getPassword().equals(hash)) {
                user = null;
            }
        }
        return user;
    }
    
    public void updatePassword(int id, String password) 
            throws SQLException, NoSuchAlgorithmException {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String hash = UserDAO.createHash(password);

        String sql = "UPDATE users SET password = ?, updated_at = ? WHERE id = ?";

        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setString(1, hash);
        statement.setTimestamp(2, now);
        statement.setInt(3, id);

        statement.executeUpdate();
        statement.close();
    }
}
