package com.example.socialnetwork.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.socialnetwork.util.DBConnection;
import com.example.socialnetwork.model.User;

public class FollowDAO {
    public void follow(int followerId, int followedId) throws SQLException {
        String sql = "INSERT INTO follows (following_user_id, followed_user_id) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            stmt.executeUpdate();
        }
    }

    public void unfollow(int followerId, int followedId) throws SQLException {
        String sql = "DELETE FROM follows WHERE following_user_id = ? AND followed_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            stmt.setInt(2, followedId);
            stmt.executeUpdate();
        }
    }

    public List<User> getFollowedUsers(int followerId) throws SQLException {
        List<User> followedUsers = new ArrayList<>();
        String sql = "SELECT u.* FROM users u JOIN follows f ON u.id = f.followed_user_id WHERE f.following_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, followerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                followedUsers.add(user);
            }
        }
        return followedUsers;
    }

    public int getFollowingCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM follows WHERE following_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public int getFollowerCount(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM follows WHERE followed_user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}