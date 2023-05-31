package com.zhang.imall.model.request;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProductListReq
 * @description : [orderBy、categoryId、keyword这三个参数可为空]
 * @createTime : [2022/10/26 22:14]
 */
public class ProductListReq {
    private String orderBy;//排序方式
    private Integer categoryId;//商品分类id
    private String keyword;//模糊查询关键词
    private Integer pageNum=1;//第几页
    private Integer pageSize=10;//每页数据量

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "ProductListReq{" +
                "orderBy='" + orderBy + '\'' +
                ", categoryId=" + categoryId +
                ", keyword='" + keyword + '\'' +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                '}';
    }
}
