package com.zhang.imall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.imall.common.Constant;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.dao.CategoryMapper;
import com.zhang.imall.model.dao.ProductMapper;
import com.zhang.imall.model.pojo.Product;
import com.zhang.imall.model.query.ProductListQuery;
import com.zhang.imall.model.request.AddProductReq;
import com.zhang.imall.model.request.ProductListReq;
import com.zhang.imall.model.request.UpdateProductReq;
import com.zhang.imall.model.vo.CategoryVO;
import com.zhang.imall.service.CategoryService;
import com.zhang.imall.service.ProductService;
import com.zhang.imall.util.URIUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author : [ZhangHewen]
 * @version : [v1.0]
 * @className : ProductServiceImpl
 * @description : [描述说明该类的功能]
 * @createTime : [2022/10/18 12:10]
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private CategoryService categoryService;

    @Override
    public void add(AddProductReq addProductReq) {
        //将实体类属性拷贝
        Product product = new Product();
        BeanUtils.copyProperties(addProductReq, product);
        //校验商品是否重名
        Product currentProduct = productMapper.selectByName(product.getName());
        if (currentProduct != null) {
            throw new ImallException(ImallExceptionEnum.NAME_EXISTED);
        }
        //执行
        int count = productMapper.insertSelective(product);
        if (count == 0) {
            throw new ImallException(ImallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public String upload(HttpServletRequest httpServletRequest, MultipartFile file) {
        //获取文件的原始名字
        String fileName = file.getOriginalFilename();
        //通过截取最后一个“.”后面的内容，获取文件扩展名,包含”.“
        String suffix = fileName.substring(fileName.lastIndexOf("."));

        //利用UUID，生成文件上传到服务器中的文件名；
        UUID uuid = UUID.randomUUID();//通过Java提供的UUID工具类，获取一个UUID；
        //把uuid和文件扩展名，拼凑成新的文件名；
        String newFileName = uuid + suffix;
        //生成文件夹的File对象；
        File fileDirectory = new File(Constant.FILE_UPLOAD_DIR_PRODUCT);
        //如果文件夹不存在的话
        if (!fileDirectory.exists()) {
            boolean mkdir = fileDirectory.mkdir();
            if (!mkdir) {
                //如果在创建这个文件夹时，创建失败，就抛出文件夹创建失败异常
                throw new ImallException(ImallExceptionEnum.MKDIR_FAILED);
            }
        }

        //生成文件的File对象；
        File destFile = new File(Constant.FILE_UPLOAD_DIR_PRODUCT + newFileName);
        //如果能执行到这儿，说明文件夹已经创建成功了；；；那么就把传过来的文件，写入到我们指定的File对象指定的位置中去；
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //执行到这儿以后，表示，我们已经把文件，存放到指定的位置了；
        //接下来，就是组织图片的url，返回给前端；
        try {
            URI hostUri = URIUtil.getHost(new URI(httpServletRequest.getRequestURL() + ""));
            return (hostUri + "/images/product/" + newFileName);
        } catch (URISyntaxException e) {
            //如果上面的过程出现了问题，就抛出文件上传失败异常；
            throw new ImallException(ImallExceptionEnum.UPLOAD_FAILED);
        }

    }

    @Override
    public void update(UpdateProductReq updateProductReq) {
        Product product = new Product();
        BeanUtils.copyProperties(updateProductReq, product);
        //检查重复名称
        Product oldProduct = productMapper.selectByName(product.getName());
        if (oldProduct != null) {
            throw new ImallException(ImallExceptionEnum.NAME_EXISTED);
        }
        int count = productMapper.updateByPrimaryKeySelective(product);
        if (count == 0) {
            throw new ImallException(ImallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public void delete(Integer id) {
        //查询是否有此产品，进行删除
        Product oldProduct = productMapper.selectByPrimaryKey(id);
        if (oldProduct == null) {
            throw new ImallException(ImallExceptionEnum.PRODUCT_NOT_EXISTED);
        }
        int count = productMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImallException(ImallExceptionEnum.DELETE_ERROR);
        }
    }

    @Override
    public void batchUpdateSellStatus(Integer[] ids, Integer sellStatus) {
        productMapper.batchUpdateSellStatus(ids, sellStatus);
    }

    /**
     * 根据商品id查询商品信息
     *
     * @param id 传来的id
     */
    @Override
    public Product detail(Integer id) {
        return productMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据页号和页数据量，展示页面
     *
     * @param pageNum  页码
     * @param pageSize 页大小
     * @return
     */
    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, "id");
        List<Product> productList = productMapper.selectForAdmin();
        PageInfo<Product> productPageInfo = new PageInfo<>(productList);
        return productPageInfo;
    }

    /**
     * 根据传来的数据进行列表展示
     *
     * @param productListReq
     * @return
     */
    @Override
    public PageInfo pageInfoList(ProductListReq productListReq) {
        //创建一个查询条件的对象
       /* private String keyWord;//搜索的关键词
        private List<Integer> categoryIds;//商品分类id*/
        ProductListQuery productListQuery = new ProductListQuery();
           /* private String orderBy;//排序方式
              private Integer categoryId;//商品分类id
              private String keyWord;//模糊查询关键词
              private Integer pageNum;//第几页
              private Integer pageSize;//每页数据量*/
        //判断是否有搜索关键词
        if (!StringUtils.isEmpty(productListReq.getKeyword())) {
            //关键词拼接成：“%keyword%”
            String keyword = new StringBuilder().append("%").append(productListReq.getKeyword()).append("%").toString();
            //将关键词条件赋值给查询对象
            productListQuery.setKeyword(keyword);
        }
        //判断传入的是否有分类id
        if (!StringUtils.isEmpty(productListReq.getCategoryId())) {
            //递归查询所有属于该目录的商品，获取当前商品分类id的所有商品
            List<CategoryVO> list = categoryService.listCategoryForCustomer(productListReq.getCategoryId());
            //使用list集合存放当前目录的所有商品id
            ArrayList<Integer> categoryIds = new ArrayList<>();
            //根目录
            categoryIds.add(productListReq.getCategoryId());
            getCategoryIds(list, categoryIds);
            //然后，把这个在查询数据库时，【当前目录和当前目录所有子目录的categoryIds】的查询条件，赋值到productListQuery查询对象上去；
            productListQuery.setCategoryIds(categoryIds);
        }
        //排序条件的处理
        //首先，尝试从productListReq这个传递参数中，获取排序的参数
        String orderBy = productListReq.getOrderBy();
        //如果我们从前端请求中的参数中，有orderBy这个有关排序的参数；
        // ；如果这个有关排序的参数，在我们预设的排序条件中的话；
        if (Constant.productListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize(), orderBy);
        } else {
            //如果前端没有传orderBy参数，或者，传递orderBy参数的值不符合我们在【Constant.ProductListOrderBy.PRICE_ASC_DESC】中定义的格式
            PageHelper.startPage(productListReq.getPageNum(), productListReq.getPageSize());
        }
        //调用Dao层编写的(可能有条件的)查询语句 productListQuery:查询关键字和id
        List<Product> productList = productMapper.selectList(productListQuery);
        PageInfo pageInfo = new PageInfo(productList);
        return pageInfo;
    }

    /**
     * 工具方法，遍历【List<CategoryVO> categoryVOList】这种递归嵌套的数据结构，获取其中所有的categoryId;
     *
     * @param categoryVOList
     * @param categoryIds
     */
    private void getCategoryIds(List<CategoryVO> categoryVOList, List<Integer> categoryIds) {
        //遍历传过来的这个【递归嵌套接口的，CategoryVOList】
        for (int i = 0; i < categoryVOList.size(); i++) {
            CategoryVO categoryVO = categoryVOList.get(i);
            if (categoryVO != null) {
                categoryIds.add(categoryVO.getId());
                //递归调用
                getCategoryIds(categoryVO.getChildCategory(), categoryIds);
            }

        }
    }

}
