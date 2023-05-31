package com.zhang.imall.common;

import com.google.common.collect.Sets;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;


/**
 * constant进行常量的放置
 */
@Component
public class Constant {
    //md5加密的盐值,尽量麻烦一点，或者直接将盐值随机生成存放到数据库
    public static final String SALT = "789asd!@#";
    //存放登陆后的用户名,使用session存储
    public static final String IMALL_USER = "IMALL_USER";
    //邮件发件人
    public static final String EMAIL_FORM = "2785015696@qq.com";
    public static final String EMAIL_SUBJECT ="【Imall】系统注册验证码" ;


    //springApplication配置上传文件的路径
    public static String FILE_UPLOAD_DIR_PRODUCT;
    @Value("${file.upload.dir.product}")
    public void setFileUploadDirProduct(String fileUploadDir) {
        FILE_UPLOAD_DIR_PRODUCT = fileUploadDir;
    }
    public static String FILE_UPLOAD_DIR_PAY;
    @Value("${file.upload.dir.pay}")
    public void setFileUploadDirPay(String fileUploadDir) {
        FILE_UPLOAD_DIR_PAY = fileUploadDir;
    }

    //升序 降序
    public interface productListOrderBy {
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price asc", "price desc");
    }

    //商品上架下架状态
    public interface SaleStatus {
        int NOT_SALE = 0;//下架
        int SALE = 1;//上架
    }

    //商品选中，未选中状态
    public interface CartIsSelected {
        int CHECKED = 1;//选中
        int UNCHECKED = 0;//未选中
    }

    //订单支付状态,枚举 订单状态默认未付款。0：取消，10：未付款，20：已付款，30：已发货，40：已完成。
    public enum OrderStatusEnum {
        CANCEL_ORDER(0, "订单已取消"),
        UNPAID_ORDER(10, "订单未支付"),
        PAID_ORDER(20, "订单已付款"),
        SHIPPED_ORDER(30, "订单已发货"),
        COMPLETE(40, "订单已完成");
        private int code;
        private String value;

        /**
         * 枚举类构造方法
         *
         * @param code  参数
         * @param value 枚举值
         */
        OrderStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        /**
         * @param enumCode 枚举参数
         * @return 枚举对象
         */
        public static OrderStatusEnum enumCheck(int enumCode) {
            for (OrderStatusEnum o : OrderStatusEnum.values()) {
                if (enumCode == o.getCode()) {
                    return o;
                }
            }
            throw new ImallException(ImallExceptionEnum.NO_ENUM);
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}