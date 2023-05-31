package com.zhang.imall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.filter.UserFilter;
import com.zhang.imall.model.dao.OrderItemMapper;
import com.zhang.imall.model.dao.OrderMapper;
import com.zhang.imall.model.dao.ProductMapper;
import com.zhang.imall.model.pojo.Order;
import com.zhang.imall.model.pojo.OrderItem;
import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.request.CreateOrderReq;
import com.zhang.imall.model.vo.CartVO;
import com.zhang.imall.model.vo.OrderItemVO;
import com.zhang.imall.model.vo.OrderVO;
import com.zhang.imall.service.CartService;
import com.zhang.imall.service.OrderService;
import com.zhang.imall.service.UserService;
import com.zhang.imall.util.OrderNumFactoryUtil;
import com.zhang.imall.util.QRCodeGenerator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : OrderServiceImpl
 * @description : [描述说明该类的功能]
 * @createTime : [2022/11/15 22:33]
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Value("${file.upload.ip}")
    String ip;

    /**
     * 创建一个订单，1.写入order表，2.写入order_item表
     * Transactional(rollbackFor = Exception.class) 开启事务
     *
     * @param createOrderReq 收件人、手机号、地址、运费、支付方式
     * @return 订单号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrder(CreateOrderReq createOrderReq) {
        //1.先拿到用户id
        Integer currentUserId = UserFilter.currentUser.getId();
        //2.下单前，查询出购物车勾选的产品集合.是否勾选。
        List<CartVO> cartVOS = cartService.list(currentUserId);
        ArrayList<CartVO> cartSelects = new ArrayList<>();
        for (CartVO cartVO : cartVOS) {//购物车中勾选的商品
            if (cartVO.getSelected() == 1) {
                cartSelects.add(cartVO);
            }
        }
        //如果没有选择商品，抛异常
        if (CollectionUtils.isEmpty(cartSelects)) {
            throw new ImallException(ImallExceptionEnum.CART_SELECT_EMPTY);
        }
        int totalPrice = 0; //订单总价
        for (CartVO cartSelect : cartSelects) {
            //1.根据购物车商品id，查询出商品
            Product product = productMapper.selectByPrimaryKey(cartSelect.getProductId());
            //2.判断商品null和是否在售
            if (product == null || !product.getStatus().equals(Constant.SaleStatus.SALE)) {
                throw new ImallException(ImallExceptionEnum.NOT_SALE);
            }
            //3.计算库存
            int newQuantity = (product.getStock()) - (cartSelect.getQuantity());
            if (newQuantity < 0) {
                throw new ImallException(ImallExceptionEnum.NOT_ENOUGH);
            }
            //生成订单的金额，便利相加
            totalPrice += cartSelect.getTotalPrice();
            //将购物车已经勾选的产品清除
            cartService.deleteCart(currentUserId, cartSelect.getProductId());
            //更新库存
            product.setStock(newQuantity);
            productMapper.updateByPrimaryKeySelective(product);
        }
        //生成订单号
        String orderNum = OrderNumFactoryUtil.createOrderCode(currentUserId);
        //创建一个订单
        Order order = new Order();
        order.setOrderNo(orderNum);//订单号
        order.setUserId(currentUserId);//用户id
        order.setTotalPrice(totalPrice);//总价
        order.setReceiverName(createOrderReq.getReceiverName());//收件人
        order.setReceiverAddress(createOrderReq.getReceiverAddress());//收件地址
        order.setReceiverMobile(createOrderReq.getReceiverMobile());//收件手机号
        order.setOrderStatus(Constant.OrderStatusEnum.UNPAID_ORDER.getCode());//订单状态默认未付款。0：取消，10：未付款，20：已付款，30：已发货，40：已完成。
        order.setPostage(createOrderReq.getCarriage());//默认运费为0
        order.setPaymentType(createOrderReq.getPaymentType());//默认在线支付1
        //创建新订单
        int count = orderMapper.insertSelective(order);
        if (count != 1) {
            throw new ImallException(ImallExceptionEnum.ORDER_CREATE_FAILED);
        }
        //将下单的商品保存到订单的商品表中，遍历购物车中已经勾选的商品
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        for (CartVO cartSelect : cartSelects) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNum);//订单商品详情，订单号
            orderItem.setProductId(cartSelect.getProductId());//商品id
            orderItem.setProductName(cartSelect.getProductName());//产品名称
            orderItem.setProductImg(cartSelect.getProductImage());//图片链接
            orderItem.setUnitPrice(cartSelect.getPrice());//每样商品的单价
            orderItem.setQuantity(cartSelect.getQuantity());//每样商品的数量
            orderItem.setTotalPrice(cartSelect.getTotalPrice());//数量*单价=总价
            //添加到已下单商品详情中
            orderItems.add(orderItem);
        }
        //写入商品订单详情表中
        for (OrderItem orderItem : orderItems) {
            int i = orderItemMapper.insertSelective(orderItem);
            if (i != 1) {
                throw new ImallException(ImallExceptionEnum.ORDER_CREATE_FAILED);
            }
        }
        return orderNum;
    }

    /**
     * @param orderNo 订单号
     * @return 订单详情
     */
    @Override
    public OrderVO detail(String orderNo) {
        //根据订单号和userId查询订单
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new ImallException(ImallExceptionEnum.NO_ORDER);
        }
        //从订单表，生成订单详情表,beanUtils.copyProperties()属于浅拷贝，就是调用set进行相同名字的属性赋值，不存在的不管
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        //查询订单详情，一个订单号可能对应多个订单详情。
        List<OrderItem> orderItemsList = orderItemMapper.selectByOrderNo(orderVO.getOrderNo());
        //遍历，将orderItem一个个的子订单，拿到其中的商品
        ArrayList<OrderItemVO> orderItemVOArrayList = new ArrayList<>();
        for (OrderItem orderItem : orderItemsList) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            //添加到前台订单详情界面的商品列表中
            orderItemVOArrayList.add(orderItemVO);
        }
        //对此订单的详情进行赋值,第147行,子商品，支付状态，
        orderVO.setOrderItemVOList(orderItemVOArrayList);
        //1.获取订单状态码
        Integer orderStatus = order.getOrderStatus();
        orderVO.setOrderStatus(orderStatus);
        //2.根据状态码获得支付状态信息
        String value = Constant.OrderStatusEnum.enumCheck(orderStatus).getValue();
        orderVO.setOrderStatusName(value);
        return orderVO;
    }

    /**
     * 分页显示数据
     * 前台订单列表进行分页显示
     *
     * @param pageNum
     * @param pageSize
     * @return 分页
     */
    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
        //获取登录用户
        Integer currentUserId = UserFilter.currentUser.getId();
        //获取用户下的订单表
        List<Order> orderList = orderMapper.selectByUserId(currentUserId);
        //需要orderVO，就是点开订单详情那个对象
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<>();
        for (Order order : orderList) {
            String orderNo = order.getOrderNo();
            OrderVO orderVO = this.detail(orderNo);
            orderVOArrayList.add(orderVO);
        }
        //开启分页，按订单时间
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<OrderVO> orderVOPageInfo = new PageInfo<>(orderVOArrayList);
        return orderVOPageInfo;
    }

    /**
     * 根据orderNo 删除记录
     * 取消订单
     *
     * @param orderNo 订单号
     */
    @Override
    public void cancel(String orderNo) {
        Integer currentUserId = UserFilter.currentUser.getId();
        Order order = orderMapper.selectByUserIdAndOrderNo(currentUserId, orderNo);
        if (order == null) {
            throw new ImallException(ImallExceptionEnum.NO_ORDER);
        }
        //订单没支付，可以取消
        if (order.getOrderStatus().equals(Constant.OrderStatusEnum.UNPAID_ORDER.getCode())) {
            //订单取消
            order.setOrderStatus(Constant.OrderStatusEnum.CANCEL_ORDER.getCode());
            //设置交易完成时间
            order.setEndTime(new Date());
            order.setUpdateTime(new Date());
            //跟新数据库
            int count = orderMapper.updateByPrimaryKeySelective(order);
            if (count != 1) {
                throw new ImallException(ImallExceptionEnum.CANCEL_ORDER_FAILED);
            }
        } else {
            throw new ImallException(ImallExceptionEnum.CANCEL_ORDER_FAILED);
        }
    }

    /**
     * 生成订单支付二维码
     *
     * @param orderNo 订单号
     * @return 二维码链接
     */
    @Override
    public String qrcode(String orderNo) {
        //首先，因为这儿是非Controller，所以，通过RequestContextHolder获取HttpServletRequest；
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //然后，拼凑订单支付url的一部分：“127.0.0.1:8083”;
        String address = ip + ":" + request.getLocalPort();
        //生成文件夹的File对象；
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR_PAY);
        //如果文件夹不存在的话
        if (!fileDirectory.exists()) {
            //如果在创建这个文件夹时，创建失败，就抛出文件夹创建失败异常
            if (!fileDirectory.mkdir()) {
                throw new ImallException(ImallExceptionEnum.MKDIR_FAILED);
            }
        }
        //然后，完整拼凑订单支付url：“http://127.0.0.1:8083//pay?orderNo=订单号”;
        //这个就是将要写到二维码中的信息；其实，也是后面的【前台：支付订单】接口的，附带了orderNo参数的完整url
        String payUrl = "http://" + address + "/order/pay?orderNo=" + orderNo;
        //然后，调用我们在QRCodeGenerator工具类中编写的，生成二维码的方法；
        try {
            QRCodeGenerator.generateQRCode(payUrl, 350, 350, Constant.FILE_UPLOAD_DIR_PAY + orderNo + ".png");
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
        //获取二维码图片的访问地址；（PS：仅仅是访问地址，而是访问地址）
        String pngAddress = "http://" + address + "/images/pay/" + orderNo + ".png";
        //然后，把这个二维码图片的访问地址返回；
        return pngAddress;
    }

    /**
     * 支付订单
     *
     * @param orderNo
     */
    @Override
    public void pay(String orderNo) {

        //先根据传入的orderNo，去尝试查询order
        Order order = orderMapper.selectByUserIdAndOrderNo(UserFilter.currentUser.getId(), orderNo);
        //如果没有找到对应的订单，就抛出“订单不存在异常”
        if (order == null) {
            throw new ImallException(ImallExceptionEnum.NO_ORDER);
        }
        //如果订单存在，就进行接下来的操作；
        //如果，订单状态还是未付款状态;那么我们就把其设为已付款状态；（其实，这一步就是付款操作）；
        //在实际开发中，这儿其实要调用支付宝或者微信等支付接口的；之后调用微信等支付接口成功后，才能够去修改订单的order_status字段；
        if (order.getOrderStatus() == Constant.OrderStatusEnum.UNPAID_ORDER.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.PAID_ORDER.getCode());//更改订单状态为已支付；
            order.setPayTime(new Date());//设置一下支付时间；
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            //如果，当前订单状态不是未付款，就抛出“当前订单状态错误”异常；
            throw new ImallException(ImallExceptionEnum.ORDER_STATUS_ERROR);
        }

    }

    /**
     * @param pageNum  当前页
     * @param pageSize 页大小
     * @return 分页
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        //然后，调用Dao层的方法，去查order表，根据userId查询List<order>；
        List<Order> orders = orderMapper.selectAllOrderForAdmin();
        //由于接口要求，返回的数据格式，需要是OrderVO；；；所以，编写工具方法：把List<Order>拼装成List<OrderVO>；
        //需要orderVO，就是点开订单详情那个对象
        ArrayList<OrderVO> orderVOList = new ArrayList<OrderVO>();
        for (Order order : orders) {
            OrderVO orderVO = this.detail(order.getOrderNo());
            orderVOList.add(orderVO);
        }
        //开启分页，按订单时间
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<OrderVO> orderVOPage = new PageInfo<>(orderVOList);
        return orderVOPage;
    }

    /**
     *
     * 对订单进行发货
     * @param orderNo 订单号
     */
    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new ImallException(ImallExceptionEnum.NO_ORDER);
        }
        //如果订单存在，就进行接下来的操作；
        //如果，订单状态是已付款，那么我们就可以发货，我们就把订单状态改为已发货；
        if (order.getOrderStatus() == Constant.OrderStatusEnum.PAID_ORDER.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.SHIPPED_ORDER.getCode());//更改订单状态为已发货；
            order.setDeliveryTime(new Date());//设置一下发货时间；
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            //如果，当前订单状态不是已付款，就抛出“当前订单状态错误”异常；
            throw new ImallException(ImallExceptionEnum.ORDER_STATUS_ERROR);
        }
    }

    /**
     * 完结订单
     * @param orderNo 订单号
     */
    @Override
    public void finish(String orderNo) {
        //先根据传入的orderNo，去尝试查询order
        Order order = orderMapper.selectByOrderNo(orderNo);
        //如果没有找到对应的订单，就抛出“订单不存在异常”
        if (order == null) {
            throw new ImallException(ImallExceptionEnum.NO_ORDER);
        }

        //如果当前登录用户是普通用户，且【要发货的订单】不属于当前登录用户：那么就抛出“订单不属于你”异常；

        if (!userService.checkAdminRole(UserFilter.currentUser) &&
                !order.getUserId().equals(UserFilter.currentUser.getId())) {
            throw new ImallException(ImallExceptionEnum.NOT_YOUR_ORDER);
        }
        //如果能通过上面的检查，那么：要么【当前登录用户是管理员】，要么【当前登录用户是普通用户；且要操作的订单，属于当前登录用户】；
        //而，上面的两种情况，都是允许完结订单的；

        //如果，订单状态是已发货，那么我们就可以完结订单；也就是，我们就可以把订单状态改为完结；
        if (order.getOrderStatus() == Constant.OrderStatusEnum.SHIPPED_ORDER.getCode()) {
            order.setOrderStatus(Constant.OrderStatusEnum.COMPLETE.getCode());//更改订单状态为完结；
            order.setEndTime(new Date());//设置一下订单完结时间；
            orderMapper.updateByPrimaryKeySelective(order);
        } else {
            //如果，当前订单状态不是已发货，就抛出“当前订单状态错误”异常；
            throw new ImallException(ImallExceptionEnum.ORDER_STATUS_ERROR);
        }
    }
}
