package com.skillassessment.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.time.LocalDate;

import com.skillassessment.DBConnection;

public class StreakDAO {

    public void updateStreak(int userId) {

        LocalDate today = LocalDate.now();

        String selectSql =
                "SELECT current_streak, longest_streak, last_activity_date "
                + "FROM student_streak WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement selectPs = con.prepareStatement(selectSql)) {

            selectPs.setInt(1, userId);

            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {

                int currentStreak = rs.getInt("current_streak");
                int longestStreak = rs.getInt("longest_streak");

                Date lastDateSql = rs.getDate("last_activity_date");

                LocalDate lastDate =
                        lastDateSql != null
                        ? lastDateSql.toLocalDate()
                        : null;

                // Already completed an activity today
                if (today.equals(lastDate)) {
                    return;
                }

                // Activity on consecutive day
                if (lastDate != null &&
                    today.equals(lastDate.plusDays(1))) {

                    currentStreak++;

                } else {

                    // Streak broken
                    currentStreak = 1;
                }

                // Update longest streak
                if (currentStreak > longestStreak) {
                    longestStreak = currentStreak;
                }

                String updateSql =
                        "UPDATE student_streak "
                        + "SET current_streak = ?, "
                        + "longest_streak = ?, "
                        + "last_activity_date = ? "
                        + "WHERE user_id = ?";

                try (PreparedStatement updatePs =
                             con.prepareStatement(updateSql)) {

                    updatePs.setInt(1, currentStreak);
                    updatePs.setInt(2, longestStreak);
                    updatePs.setDate(3, Date.valueOf(today));
                    updatePs.setInt(4, userId);

                    updatePs.executeUpdate();
                }

            } else {

                // First activity of the student
                String insertSql =
                        "INSERT INTO student_streak "
                        + "(user_id, current_streak, longest_streak, last_activity_date) "
                        + "VALUES (?, 1, 1, ?)";

                try (PreparedStatement insertPs =
                             con.prepareStatement(insertSql)) {

                    insertPs.setInt(1, userId);
                    insertPs.setDate(2, Date.valueOf(today));

                    insertPs.executeUpdate();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public int getCurrentStreak(int userId) {

        String sql =
                "SELECT current_streak " +
                "FROM student_streak " +
                "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("current_streak");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    public int getLongestStreak(int userId) {

        String sql =
                "SELECT longest_streak " +
                "FROM student_streak " +
                "WHERE user_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("longest_streak");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}