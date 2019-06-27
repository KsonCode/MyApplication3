package com.bwie.myapplication.entity;

import java.util.List;

public class ProductEntity {
    public String message;
    public String status;
    public List<Product> result;

    public static class Product{
        public String commodityName;
    }
}
