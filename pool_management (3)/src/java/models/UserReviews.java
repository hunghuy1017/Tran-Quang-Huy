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
public class UserReviews {

    int ReviewID, UserID, PoolID, Rating;
    String Comment;
    Timestamp CreatedAt;
    String userName, userImage;

    public UserReviews() {
    }

    public UserReviews(int ReviewID, int UserID, int PoolID, int Rating, String Comment, Timestamp CreatedAt, String userName, String userImage) {
        this.ReviewID = ReviewID;
        this.UserID = UserID;
        this.PoolID = PoolID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
        this.userName = userName;
        this.userImage = userImage;
    }

    public UserReviews(int UserID, int PoolID, int Rating, String Comment, Timestamp CreatedAt) {
        this.UserID = UserID;
        this.PoolID = PoolID;
        this.Rating = Rating;
        this.Comment = Comment;
        this.CreatedAt = CreatedAt;
    }

    public UserReviews(int ReviewID, int UserID, int PoolID, int Rating, String Comment, Timestamp CreatedAt) {
        this.ReviewID = ReviewID;
        this.UserID = UserID;
        this.PoolID = PoolID;
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

    public int getPoolID() {
        return PoolID;
    }

    public void setPoolID(int PoolID) {
        this.PoolID = PoolID;
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

    @Override
    public String toString() {
        return "UserReviews{" + "ReviewID=" + ReviewID + ", UserID=" + UserID + ", PoolID=" + PoolID + ", Rating=" + Rating + ", Comment=" + Comment + ", CreatedAt=" + CreatedAt + ", userName=" + userName + ", userImage=" + userImage + '}';
    }
}
