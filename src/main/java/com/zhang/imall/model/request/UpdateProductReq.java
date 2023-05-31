package com.zhang.imall.model.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : UpdateProductReq
 * @description : [描述说明该类的功能]
 * @createTime : [2022/10/18 21:14]
 */
public class UpdateProductReq {
    @NotNull(message = "商品id不能为空")
    private Integer id;
    private String name;
    private String image;
    //详情描述
    private String detail;
    private Integer categoryId;
    @Min(value = 1, message = "价格不能小于1")
    private Integer price;
    @Max(value = 10000, message = "库存不能大于10000")
    private Integer stock;
    //状况：1上架  0下架
    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateProductReq{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", detail='" + detail + '\'' +
                ", categoryId=" + categoryId +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}

