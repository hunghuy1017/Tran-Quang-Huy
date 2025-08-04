/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.sql.Timestamp;

/**
 *
 * @author THIS PC
 */
public class TrainerReviews {
    int ReviewID, UserID, TrainerID, Rating;
    String Comment;
    Timestamp CreatedAt;
    String userName, userImage;

    public TrainerReviews() {
    }

    public TrainerReviews(int ReviewID, int UserID, int TrainerID, int Rating, String Comment, Timestamp CreatedAt, String userName, String userImage) {
        this.ReviewID = ReviewID;
        this.UserID = UserID;
        this.TrainerID = TrainerID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
        this.userName = userName;
        this.userImage = userImage;
    }

    public TrainerReviews(int UserID, int TrainerID, int Rating, String Comment, Timestamp CreatedAt, String userName, String userImage) {
        this.UserID = UserID;
        this.TrainerID = TrainerID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
        this.userName = userName;
        this.userImage = userImage;
    }

    public TrainerReviews(int ReviewID, int UserID, int TrainerID, int Rating, String Comment, Timestamp CreatedAt) {
        this.ReviewID = ReviewID;
        this.UserID = UserID;
        this.TrainerID = TrainerID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
    }

    public TrainerReviews(int UserID, int TrainerID, int Rating, String Comment, Timestamp CreatedAt) {
        this.UserID = UserID;
        this.TrainerID = TrainerID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
    }
    
    

    public int getReviewID() {
        return ReviewID;
    }

    public void setReviewID(int ReviewID) {
        this.ReviewID = ReviewID;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int UserID) {
        this.UserID = UserID;
    }

    public int getTrainerID() {
        return TrainerID;
    }

    public void setTrainerID(int TrainerID) {
        this.TrainerID = TrainerID;
    }

    public int getRating() {
        return Rating;
    }

    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String Comment) {
        this.Comment = Comment;
    }

    public Timestamp getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Timestamp CreatedAt) {
        this.CreatedAt = CreatedAt;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    
}
