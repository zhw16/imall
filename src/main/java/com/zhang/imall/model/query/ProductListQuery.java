package com.zhang.imall.model.query;

import java.util.List;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProductListQuery
 * @description : [描述：查询商品列表的Query]
 * @createTime : [2022/10/26 22:20]
 */
public class ProductListQuery {
    private String keyword;//搜索的关键词
    private List<Integer> categoryIds;//商品分类id

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public List<Integer> getCategoryIds() {
        return categoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        this.categoryIds = categoryIds;
    }

    @Override
    public String toString() {
        return "ProductListQuery{" +
                "keyword='" + keyword + '\'' +
                ", categoryIds=" + categoryIds +
                '}';
    }
}
