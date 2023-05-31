package com.zhang.imall.controller;

import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.ApiRestResponse;
import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.pojo.Category;
import com.zhang.imall.model.pojo.User;
import com.zhang.imall.model.request.AddCategoryReq;
import com.zhang.imall.model.request.UpdateCategoryReq;
import com.zhang.imall.model.vo.CategoryVO;
import com.zhang.imall.service.CategoryService;
import com.zhang.imall.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

/**
 * 商品分类的controller，目录controller
 */
@Controller
public class CategoryController {
    //注入service层接口
    @Resource
    private UserService userService;
    @Resource
    private CategoryService categoryService;

    /**
     * 添加商品分类
     *
     * @param session        判断登录，和用户角色
     * @param addCategoryReq 新建的实体类保存，前端传来的参数，封装,已经在实体对象对AddCategoryReq的参数进行限制
     * @return 操作信息
     * 在参数里面 加上@RequestBody，要进行测试们就要在postman的body里面拼接字符json
     */
    @ApiOperation("添加商品种类目录")//接口描述信息
    @PostMapping("admin/category/add")
    @ResponseBody
    //@Valid校验实体对象是否有效，这样在bean里面进行@NotNull校验后，就不用进行if的空判断了。
    // 可是@Valid抛出的异常很宽泛MethodArgumentNotValidException，需要自己进行异常的定义。
    //Spring Boot2.3版本将不再内部依赖validator；所以对于2.3以后的版本，如果还想使用@Valid参数校验，需要自己先手动引入validator依赖。
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
        /*if (addCategoryReq.getName() == null || addCategoryReq.getType() == null || addCategoryReq.getParentId() == null || addCategoryReq.getOrderNum() == null) {
            //商品分类的名字、类别、父亲id（如果是最顶层，父id=0）、等参数不能为空
            return ApiRestResponse.error(ImallExceptionEnum.CATEGORY_PARA_NEED_NOT_NULL);
        }*/
        //需要验证登录,User对象不能为空,需要登录
        User currentUser = (User) session.getAttribute(Constant.IMALL_USER);
        if (currentUser == null) {
            //用户未登录
            return ApiRestResponse.error(ImallExceptionEnum.USER_NEED_LOGIN);
        }
        //验证用户有管理员权限，2->true
        boolean adminRole = userService.checkAdminRole(currentUser);
        if (adminRole) {
            //插入种类数据，成功
            categoryService.addCategory(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            //需要管理员登陆
            return ApiRestResponse.error(ImallExceptionEnum.NEED_ADMIN_USER);
        }
    }

    /**
     * @param session           管理员权限更新数据
     * @param updateCategoryReq 前端封装好的更新的数据
     * @return 更新的信息
     */
    @ApiOperation("更新产品的分类目录信息")
    @PostMapping("/admin/category/update")
    @ResponseBody
    public ApiRestResponse updateCategory(HttpSession session, @Valid @RequestBody UpdateCategoryReq updateCategoryReq) {
        //验证登录信息
        User currentUser = (User) session.getAttribute(Constant.IMALL_USER);
        if (currentUser == null) {//用户未登录
            return ApiRestResponse.error(ImallExceptionEnum.USER_NEED_LOGIN);
        }
        if (userService.checkAdminRole(currentUser)) {//验证管理员信息
            Category category = new Category();
            //源：传来的数据；目标：新建的Category对象
            BeanUtils.copyProperties(updateCategoryReq, category);
            //执行
            categoryService.update(category);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(ImallExceptionEnum.NEED_ADMIN_USER);
        }
    }

    /**
     * 删除产品分类信息
     *
     * @return
     */
    @ApiOperation("删除商品分类信息")
    @PostMapping("/admin/category/delete")
    @ResponseBody
    public ApiRestResponse deleteCategory(@RequestParam("id") Integer id) {
        categoryService.delete(id);
        //删除成功
        return ApiRestResponse.success();
    }

    /**
     * 分页的操作
     *
     * @param pageNum  第几页
     * @param pageSize 每页多少数据
     * 只有两个参数就不使用JavaBean接受
     * @return 数据的响应的参数和返回的数据
     */
    @ApiOperation("后台分类目录的列表分页")
    @PostMapping("/admin/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForAdmin(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        PageInfo pageInfo = categoryService.listCategoryForAdmin(pageNum, pageSize);
        //返回当前页码的数据
        return ApiRestResponse.success(pageInfo);
    }


    /**
     * 前台商品分类的方法
     * 分类目录列表
     */
    @ApiOperation("前台分类目录列表")
    @GetMapping("/category/list")
    @ResponseBody
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
