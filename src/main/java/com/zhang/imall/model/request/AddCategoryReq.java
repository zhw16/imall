package com.zhang.imall.model.request;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
/**
 * 防止controller层的参数过多，提前进行封装
 * 添加前端目录的请求实体类bean，接受参数
 * 添加后台商品目录分类的实体请求
 *
 * @Max(10) 最大不超过10
 * @Size( min = 1,max = 10) - ->长度在1-10之间
 * @NotNull 非空
 * @Valid  参数是有效的-->可以放在@RequestBody前，检验传过来的实体对象有效
 */

public class AddCategoryReq {
    //分类目录名称
    @NotNull(message = "name不能为null")
    //name长度不小于2，不大于5
    @Size(min = 2, max = 5)
    private String name;
    //分类目录级别，1代表一级；2 代表二级
    @NotNull(message = "type不能为null")
    @Max(5)
    private Integer type;
    //父级目录id，如果自己是最顶级目录，则0
    @NotNull(message = "parentId不能为null")
    private Integer parentId;
    //目录展示时的排序
    @NotNull(message = "orderNum不能为null")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }
}
