package com.zhang.imall.model.vo;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : CartVO
 * @description : [前端封装购物车的属性,包含了购物车表和商品表，用来承载多表查询的结果.
 * @createTime : [2022/10/31 15:47]
 */
public class CartVO {
    //购物车id
    private Integer id;
    //产品id
    private Integer productId;
    //用户id
    private Integer userId;
    //数量
    private Integer quantity;
    //是否被选中
    private Integer selected;
    //单价
    private Integer price;
    //总价
    private Integer totalPrice;
    //产品名称
    private String productName;
    //产品图片
    private String productImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getSelected() {
        return selected;
    }

    public void setSelected(Integer selected) {
        this.selected = selected;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getTotalPrice() {//总价
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @Override
    public String toString() {
        return "CartVO{" +
                "id=" + id +
                ", productId=" + productId +
                ", userId=" + userId +
                ", quantity=" + quantity +
                ", selected=" + selected +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", productName='" + productName + '\'' +
                ", productImage='" + productImage + '\'' +
                '}';
    }
}
