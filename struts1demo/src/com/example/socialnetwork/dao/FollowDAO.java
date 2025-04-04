package com.example.socialnetwork.dao;

import com.example.socialnetwork.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class FollowDAO {

    // Thêm mối quan hệ theo dõi
    public void followUser(int followingUserId, int followedUserId) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO follows (following_user_id, followed_user_id, created_at) VALUES (?, ?, CURRENT_TIMESTAMP)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, followingUserId);
            stmt.setInt(2, followedUserId);
            stmt.executeUpdate();
        }
    }

    // Xóa mối quan hệ theo dõi
    public void unfollowUser(int followingUserId, int followedUserId) throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM follows WHERE following_user_id = ? AND followed_user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, followingUserId);
            stmt.setInt(2, followedUserId);
            stmt.executeUpdate();
        }
    }
}