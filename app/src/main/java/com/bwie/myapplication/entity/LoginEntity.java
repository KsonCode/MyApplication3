package com.bwie.myapplication.entity;

public class LoginEntity {
    public String message;
    public String status;
    public User result;

    public static class User{
        public String phone;
        public String sessionId;
    }
}
