package com.design_shinbi.tsubuyaki.model.dao;

import java.security.NoSuchAlgorithmException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.design_shinbi.tsubuyaki.model.PostInfo;
import com.design_shinbi.tsubuyaki.model.entity.Image;
import com.design_shinbi.tsubuyaki.model.entity.Message;
import com.design_shinbi.tsubuyaki.model.entity.User;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public void add(int userId, String text) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        String sql = "INSERT INTO messages (user_id, text, created_at, updated_at) "
                + "VALUES(?, ?, ?, ?)";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setInt(1, userId);
        statement.setString(2, text);
        statement.setTimestamp(3, now);
        statement.setTimestamp(4, now);

        statement.executeUpdate();
        statement.close();
    }

    public void update(Message message) throws SQLException {
        Timestamp now = new Timestamp(System.currentTimeMillis());

        String sql = "UPDATE messages SET user_id = ?, text = ?, "
                + "updated_at = ? WHERE id = ?";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setInt(1, message.getUserId());
        statement.setString(2, message.getText());
        statement.setTimestamp(3, now);
        statement.setInt(4, message.getId());

        statement.executeUpdate();
        statement.close();
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM messages WHERE id = ?";

        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setInt(1, id);

        statement.executeUpdate();
        statement.close();
    }

    private Message createMessage(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getInt("id"));
        message.setUserId(resultSet.getInt("user_id"));
        message.setText(resultSet.getString("text"));
        message.setCreatedAt(resultSet.getTimestamp("created_at"));
        message.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return message;
    }

    public List<Message> findAll() throws SQLException {
        List<Message> list = new ArrayList<Message>();

        String sql = "SELECT * FROM messages";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Message message = createMessage(resultSet);
            list.add(message);
        }
        resultSet.close();
        statement.close();

        return list;
    }

    public Message find(int id) throws SQLException {
        Message message = null;

        String sql = "SELECT * FROM messages WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            message = createMessage(resultSet);
        }
        resultSet.close();
        statement.close();

        return message;
    }

    public int getMaxId() throws SQLException {
        String sql = "SELECT MAX(id) AS max FROM messages";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        int id = 0;
        if (resultSet.next()) {
            id = resultSet.getInt("max");
        }
        resultSet.close();
        statement.close();
        
        return id;
    }
    
    public void setImage(int id, Image image) throws SQLException {
        String sql = "UPDATE messages SET image = ?, image_file_name = ? WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setBlob(1, image.getStream());
        statement.setString(2, image.getFileName());
        statement.setInt(3, id);

        statement.executeUpdate();
        statement.close();
    }
    
    public void deleteImage(int id)    throws SQLException {
        String sql = "UPDATE messages SET image = ?, image_file_name = ? WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setNull(1, Types.BLOB);
        statement.setNull(2, Types.VARCHAR);
        statement.setInt(3, id);

        statement.executeUpdate();
        statement.close();
    }
    
    public Image getImage(int id) throws SQLException {
        String sql = "SELECT image, image_file_name FROM messages WHERE id = ?";
        PreparedStatement statement = this.connection.prepareStatement(sql);

        statement.setInt(1, id);

        Image image = null;
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()) {
            String fileName = resultSet.getString("image_file_name");
            Blob blob = resultSet.getBlob("image");

            if (blob != null) {
                image = new Image(fileName, blob.getBinaryStream());
            }
        }

        resultSet.close();
        statement.close();

        return image;
    }
    
    /*public List<PostInfo> getPosts() 
            throws SQLException, NoSuchAlgorithmException {
        UserDAO userDao = new UserDAO(this.connection);
        List<Message> messages = this.findAll();
        
        List<PostInfo> list = new ArrayList<PostInfo>();
        
        for(Message message : messages) {
            User user = userDao.find(message.getUserId());
            Image image = this.getImage(message.getId());
            
            PostInfo post = new PostInfo(user, message);
            if (image != null) {
                post.setImage(image);
            }
            list.add(post);
        }
        
        return list;
    }
}*/
    public List<PostInfo> getPosts() throws SQLException, NoSuchAlgorithmException {
        UserDAO userDao = new UserDAO(this.connection);
        List<Message> messages = new ArrayList<>();

        String sql = "SELECT * FROM messages ORDER BY created_at DESC";
        Statement statement = this.connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql);
        while (resultSet.next()) {
            Message message = createMessage(resultSet);
            messages.add(message);
        }
        resultSet.close();
        statement.close();

        List<PostInfo> list = new ArrayList<>();
        
        for (Message message : messages) {
            User user = userDao.find(message.getUserId());
            Image image = this.getImage(message.getId());
            
            PostInfo post = new PostInfo(user, message);
            if (image != null) {
                post.setImage(image);
            }
            list.add(post);
        }
        
        return list;
    }
}
   