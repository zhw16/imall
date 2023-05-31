package com.zhang.imall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zhang.imall.exception.ImallException;
import com.zhang.imall.exception.ImallExceptionEnum;
import com.zhang.imall.model.dao.CategoryMapper;
import com.zhang.imall.model.pojo.Category;
import com.zhang.imall.model.request.AddCategoryReq;
import com.zhang.imall.model.vo.CategoryVO;
import com.zhang.imall.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

//注解表示为service层，目录分类的实现类
@Service("categoryService")

public class CategoryServiceImpl implements CategoryService {

    //注入mapper
    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 添加商品目录种类
     *
     * @param addCategoryReq 前端传递的表单封装的实体
     */
    @Override
    public void addCategory(AddCategoryReq addCategoryReq) {
        //新建一个category对象
        Category category = new Category();
        //把request对象里面的参数保存到新的category对象里面
       /* category.setName(addCategoryReq.getName());
        category.setType(addCategoryReq.getType());
        category.setParentId(addCategoryReq.getParentId());
        category.setOrderNum(addCategoryReq.getOrderNum());*/

        //这样就不用一个一个去设置属性,把AddCategory里面的属性，赋值给Category
        BeanUtils.copyProperties(addCategoryReq, category);
        //查询是否已经存在这个种类
        Category categoryOld = categoryMapper.selectByName(category.getName());
        if (categoryOld != null) {
            throw new ImallException(ImallExceptionEnum.CATEGORY_NAME_EXISTED);
        }else if (category.getParentId() != 0 || category.getType() != 1) {
            Category parentCategory = categoryMapper.selectByPrimaryKey(category.getParentId());
            if (parentCategory == null || !parentCategory.getType().equals(category.getType() - 1))
                throw new ImallException(ImallExceptionEnum.PARENT_ID_NOT_EXISTED);
        }
        //校验完成，写入数据库
        int count = categoryMapper.insertSelective(category);
        if (count == 0) {
            //插入失败，异常后终止
            throw new ImallException(ImallExceptionEnum.CATEGORY_CREATE_FAILED);
        }
    }

    /**
     * 更新商品目录
     *
     * @param updateCategory controller层传递来的数据，要更新的数据
     */
    @Override
    public void update(Category updateCategory) {

        if (updateCategory.getName() != null) {
            //校验，先查询要更新的数据的名字是否冲突
            Category categoryOld = categoryMapper.selectByName(updateCategory.getName());
            //可以按照新的名字查到有名字一样的，（要修改的数据就是一条），但是要保证id不一样就不会查到要修改的数据
            if (categoryOld != null && !categoryOld.getId().equals(updateCategory.getId())) {
                //id不同，并且要修改的名字已存在，重名操作失败
                throw new ImallException(ImallExceptionEnum.NAME_EXISTED);
            } else if (updateCategory.getParentId() != 0 || updateCategory.getType() != 1) {
                Category parentCategory = categoryMapper.selectByPrimaryKey(updateCategory.getParentId());
                if (parentCategory == null || !parentCategory.getType().equals(updateCategory.getType() - 1))
                    throw new ImallException(ImallExceptionEnum.PARENT_ID_NOT_EXISTED);
            } else {//更新
                int count = categoryMapper.updateByPrimaryKeySelective(updateCategory);
                if (count == 0) {//更新失败
                    throw new ImallException(ImallExceptionEnum.UPDATE_FAILED);
                }
            }
        }
    }

    /**
     * 通过id删除数据
     *
     * @param id 前端传来的要删除id
     */
    @Override
    public void delete(Integer id) {
        //先通过id查询数据是否在
        Category categoryOld = categoryMapper.selectByPrimaryKey(id);
        if (categoryOld == null) {
            throw new ImallException(ImallExceptionEnum.DELETE_ERROR);
        }
        int count = categoryMapper.deleteByPrimaryKey(id);
        if (count == 0) {
            throw new ImallException(ImallExceptionEnum.DELETE_ERROR);
        }
    }

    /**
     * 实现管理员商品目录分页的操作
     * @param pageNum 页码
     * @param pageSize 一页的大小
     * @return 按照pageHelper规则生成的页数据
     */
    @Override
    public PageInfo listCategoryForAdmin(Integer pageNum, Integer pageSize) {
        //查询结果先按照type进行排列，再按照order_num 的顺序进行排列
         PageHelper.startPage(pageNum, pageSize,"type,order_num");
         //调用dao层的方法去查询所有的数据
        List<Category> categoryList = categoryMapper.selectList();
        //封装当前pageHelper的规则的数据
        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        return pageInfo;
    }

    /**
     * 递归调用
     * 前台的分类目录，针对前台的
     * @return
     */
    @Override
    //开启redis缓存数据，使用的key就是方法名，value就是返回值。调用方法时先尝试查看redis中是否有数据。
    @Cacheable(value = "listCategoryForCustomer")
    public List<CategoryVO> listCategoryForCustomer(Integer id) {
        //定义一个List，这个List就用来存在最终的查询结果；即，这个List中的直接元素是：所有的parent_id=0，即type=1的，第1级别的目录；
        ArrayList<CategoryVO> categoryVOList = new ArrayList<>();
        //我们额外创建recursivelyFindCategories()方法，去实现递归查询的逻辑；
        //我们第一次递归查询时，是先查一级目录；（而一级目录的parentId是0）
        //该方法第一个参数是：List<CategoryVO> categoryVOList：用来存放当前级别对应的，所有的下一级目录数据；
        //  PS：对于【最终返回给前端的List<CategoryVO> categoryVOList】来说，其所谓的下一级目录就是：所有的parent_id=0，即type=1的，第1级别的目录；
        //  PS：对于【所有的parent_id=0，即type=1的，第1级别的目录；】来说，其categoryVOList就是【List<CategoryVO> childCategory属性】，其是用来存放该级别对应的所有的parent_id=1，即type=2的，第2级别的目录；
        //  PS：对于【所有的parent_id=1，即type=2的，第2级别的目录；】来说，其categoryVOList就是【List<CategoryVO> childCategory属性】，其是用来存放该级别对应的所有的parent_id=2，即type=3的，第3级别的目录；
        //该方法的第二个参数是：当前级别目录的parent_id，即也就是当前级别的上一级目录的id；
        //即，第一个参数是【上一级别的List<CategoryVO> categoryVOList】；第二参数是【下一级别的parent_id，也就是当前级别的id】；
        recursivelyFindCategories(categoryVOList, id);
        return categoryVOList;
    }

    private void recursivelyFindCategories(List<CategoryVO> categoryVOList, Integer parentId) {
        //首先，根据parent_id，查询出所有该级别的数据；（比如，第一次我们查询的是parent_id=0，即type=1的，第1级别的目录）
        List<Category> categoryList = categoryMapper.selectCategoriesByParentId(parentId);
        if (!CollectionUtils.isEmpty(categoryList)) {
            //便利上边查到的当前级别的数据，将其加入到对应上级目录的List<CategoryVO> childCategory属性中
            for (int i = 0; i < categoryList.size(); i++) {
                //获取到【上面查询的，该级别数据中的，一条数据】，把其存储到上级目录的List<CategoryVO> childCategory属性中；
                //自然，如果该级别是【parent_id=0，即type=1的，第1级别的目录】，就是把其存储在最顶级的、返回给前端的那个List<CategoryVO> categoryVOS中；
                Category category = categoryList.get(i);
                CategoryVO categoryVO = new CategoryVO();
                BeanUtils.copyProperties(category, categoryVO);
                categoryVOList.add(categoryVO);
                //然后，这一步是关键：针对【每一个当前级别的，目录数据】去递归调用recursivelyFindCategories()方法；
                //自然，第一个参数是【当前级别数据的，List<CategoryVO> childCategory属性】：这是存放所有下级别目录数据的；
                //第二个参数是【当前级别数据的id】：这自然是下级别目录数据的parent_id:
                recursivelyFindCategories(categoryVO.getChildCategory(),categoryVO.getId());
            }
        }
    }

}
