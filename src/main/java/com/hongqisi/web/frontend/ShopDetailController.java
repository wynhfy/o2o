package com.hongqisi.web.frontend;


import com.hongqisi.dto.ProductExecution;
import com.hongqisi.entity.Product;
import com.hongqisi.entity.ProductCategory;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.service.ProductCategoryService;
import com.hongqisi.service.ProductService;
import com.hongqisi.service.ShopCategoryService;
import com.hongqisi.service.ShopService;
import com.hongqisi.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/frontend")
public class ShopDetailController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ProductCategoryService productCategoryService;
    @Autowired
    private ProductService productService;

    /**
     * 获取店铺信息以及该店铺下面的商品类别列表
     */
    @RequestMapping(value = "/listshopdetailpageinfo" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShopDetailPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        long shopId= HttpServletRequestUtil.getLong(request,"shopId");
        List<ProductCategory> productCategoryList=null;
        if(shopId>-1){
            Shop shop=shopService.getShopById(shopId);
            productCategoryList=productCategoryService.getProductCategoryList(shopId);
            modelMap.put("shop",shop);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg:","empty shop");
        }
        return modelMap;
    }

    /**
     * 依据查询条件分页列出该店铺下面的所有商品
     */
     @RequestMapping(value = "/listproductsbyshop",method = RequestMethod.GET)
     @ResponseBody
     public Map<String,Object> listProductByShopId(HttpServletRequest request){
         Map<String,Object> modelMap=new HashMap<>();
         int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
         int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
         long shopId=HttpServletRequestUtil.getLong(request,"shopId");
         if((pageIndex>-1)&&(pageSize>-1)&&(shopId>-1)){
             int productCategoryId=HttpServletRequestUtil.getInt(request,"productCategoryId");
             String productName=HttpServletRequestUtil.getString(request,"productName");
             Product product=CompactProductCondition4Search(shopId,productCategoryId,productName);
             ProductExecution pe=productService.getProductList(product,pageIndex,pageSize);
             modelMap.put("productList",pe.getProductList());
             modelMap.put("count",pe.getCount());
             modelMap.put("success",true);
         }else{
             modelMap.put("success",false);
             modelMap.put("errMsg:","empty shopId");
         }
         return modelMap;
     }

    /**
     * 组合查询条件
     * @param shopId
     * @param productCategoryId
     * @param productName
     * @return
     */
    private Product CompactProductCondition4Search(long shopId,int productCategoryId,String productName){
        Shop shop=new Shop();
        shop.setShopId(shopId);
        Product product=new Product();
        product.setShop(shop);
        if(productCategoryId!=-1L){
            ProductCategory productCategory=new ProductCategory();
            productCategory.setProductCategoryId(productCategoryId);
            product.setProductCategory(productCategory);
        }
        if(productName!=null){
            product.setProductName(productName);
        }
        product.setEnableStatus(1);
        return product;
    }


}
