# imall项目说明
1.数据库 
2.日志文件  
3.上传文件路径  
4.redis 配置
5.ip改为生产环境IP
## 项目背景：属于电商系统的实现。

## Controller的API接口：http://localhost:8000/swagger-ui.html

### 测试模块：
* 测试数据链路接口：method=GET， URL:http://localhost:8000/user/user_test, ARGS：“null”；说明：测试接口在service中通过id查询用户信息，返回json格式的数据到浏览器。

### 用户模块：

* 用户注册界面：method=POST, URL:http://localhost:8000/user/register,
  ARGS：“username=111122,password=12345678”；说明：通过postman发送post请求，返回状态信息和状态码。
* 用户登录界面：method=post, URL:http://localhost:8000/user/login,
  ARGS：“username=11122，password=12345678”；说明：通过postman发送post请求，返回状态数据，里面包含user的实体类，不包含password。
* 更新签名：method=post, URL:http://localhost:8000/user/update, ARGS:"HttpSession=当前登录人，signatu=”签名“；
  说明：通过当前的登陆的用户名id，查询后更新签名。
* 登出账户：method=post, URL:http://localhost:8000/user/logout, ARGS:HttpSession="当前登录的用户"； 说明：查询到当前session里面的用户，进行注销。
* 管理员登陆：method=post, URL:http://localhost:8000/user/adminLogin, ARGS:“username=11122，password=12345678”;
  说明：查询后，通过判断返回用户的role字段，确认是否为管理员。

### 商品分类管理模块(使用的是@RequestBody 使用postman需要在body里面拼接json字符串)

* 增加分类：http://localhost:8000/admin/category/add，  ARGS:AddCategoryReq对象包含："name":"冰激淋","type":"5", "parentId":6,"orderNum":
  15；说明，先校验管理员权限，然后封装四个参数到对象(使用的是@RequestBody 使用postman需要在body里面拼接json字符串)。
* 修改分类：http://localhost:8000/admin/category/update， ARGS:UpdateCategoryReq对象包含：”id“:非空"，name":,"type":"5", "parentId":6,"
  orderNum":15；说明，先校验管理员权限，然后封装1+n参数到对象(使用的是@RequestBody 使用postman需要在body里面拼接json字符串)。
* 删除分类：http://localhost:8000/admin/category/delete， ARGS：id=30, 需要管理员登录
* 显示所有的明细，分页显示：http://localhost:8000/admin/category/list， ARGS：pageNum=1&pageSize=10，需要管理员登录。使用pagehelper插件，返回pageInfo对象。
* 前台分类目录列表：http://localhost:8000/category/list ，ARGS：null，METHOD：get, 说明：前台商品分类目录。

### 后台管理商品
* 添加商品 http://localhost:8000/admin/product/add  参数：{ "name":"名称","image":"图片","detail":"详情（可为空）","categoryId":"0","price":"1","stock":"12","status":"0"}
* 上传商品图片：http://localhost:8000/admin/upload/file 参数：在body里面form_data里面file 选择图片。经过资源映射，得到链接.
* 更新商品：http://localhost:8000/admin/product/update 参数："id"（必传的） "name":"名称1","image":"图片","detail":"详情","categoryId":"0","price":,"stock":,"status":
* 删除商品：http://localhost:8000/admin/product/delete 参数：要删除的id.
* 批量上架下架商品: http://localhost:8000/admin/product/batchUpdateSellStatus?ids=41,40&sellStatus=0 数组保存 ids=41,40.
* 根据id查询产品详细信息：http://localhost:8000/product/detail?id=42
* 传入pageNum和pageSize得到当前页get方法： http://localhost:8000/admin/product/list

## 前台商品模块
* 前台查看当前目录下的所有商品，支持模糊查询和排序get： http://localhost:8000/product/list 前端使用get请求，body里面没有东西，不使用@REquestBody
  http://localhost:8000/product/list?orderBy=price asc/desc&categoryId=3&keyword=米&pageNum=2&pageSize=20

## 购物车模块
* 加入商品：把商品加入到购物车；返回的是购物车内容；http://localhost:8000/cart/add；参数：产品id，数量。从过滤器获得用户id，需要登录。
* 列表显示：显示购物车列表；http://localhost:8000/cart/list ，不需要参数，需要用户登录，get请求。
* 商品数量更改： http://localhost:8000/cart/update?productId=21&newCount=1  ,需要登录，post
* 删除商品：http://localhost:8000/cart/delete,post,productId.
* 勾选、反选；http://localhost:8000/cart/updateStatus,post,参数：productId和selectStatus。产品id和状态。
* 全选、全不选；http://localhost:8000/cart/selectAll?selectStatus=0 ,设置购物车全部产品状态为全选和全不选。
## 订单模块--前台订单模块
* 下单：POST： http://localhost:8000/order/create；ARGS:CreateOrderReq对象包含：”receiverName“:非空"，receiverMobile":"非空"，receiverAddre:非空,carriage, paymentType 
说明，先校验用户登录，然后封装参数到对象(使用的是@RequestBody 使用postman需要在body里面拼接json字符串)。同时开启数据库事务。
* 订单列表：GET：http://localhost:8000/order/list,ARGS：pageNum,pageSize
* 订单详情：GET：http://localhost:8000/order/detail；ARGS：orderNo；展示订单的收获信息，支付信息和订单的商品列表，
* 取消订单:POST http://localhost:8000/order/cancel ;ARGS:orderNo,取消未支付的订单。
* 支付二维码：GET: http://localhost:8000/order/qrcode?orderNo=20221117162112101117278
* 扫码支付:http://localhost:8000/order/pay?orderNo=20221117162112101117278
* 确认收货：http://localhost:8000/order/finish?orderNo=20221117162112101117278,前后端通用
## 订单模块--后台订单模块
* 订单列表：POST http://localhost:8000/admin/order/list ,ARGS:null
* 发货:POST http://localhost:8000/admin/order/delivered?orderNo=20221117162112101117278
* 订单完结:http://localhost:8000/order/finish?orderNo=20221117162112101117278,前后端通用
* 
### 注意事项：

#### 使用过滤器完成管理员的校验：

* 1.其他：【J2EE中的过滤器 Filter】或【Spring中的AOP 】或【Spring MVC中的拦截器】，此处使用j2ee过滤器完成管理员的校验。
* 1. 过滤器（Filter）能拿到http请求，但是拿不到处理请求方法的信息。
* 2. 拦截器（Interceptor）既能拿到http请求信息，也能拿到处理请求方法的信息，但是拿不到方法的参数信息。
* 3. 切片（Aspect）能拿到方法的参数信息，但是拿不到http请求信息。
* 他们三个各有优缺点，需要根据自己的业务需求来选择最适合的拦截机制。

### redis的使用缓存。
* 引入pom的依赖



