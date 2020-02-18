package com.hongqisi.web.productCategory;

import com.hongqisi.dto.ProductCategoryExecution;
import com.hongqisi.dto.Result;
import com.hongqisi.entity.ProductCategory;
import com.hongqisi.entity.Shop;
import com.hongqisi.enums.ProductCategoryStateEnum;
import com.hongqisi.exceptions.ProductCategoryOperationException;
import com.hongqisi.service.ProductCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/shop")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @RequestMapping(value = "/getproductcategorylist",method = RequestMethod.GET)
    @ResponseBody
    public Result<List<ProductCategory>> getProductCategoryByShopId(HttpServletRequest request){
        Shop shop=new Shop();
        shop.setShopId(1L);
        request.getSession().setAttribute("currentShop",shop);

        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        List<ProductCategory> list=null;
        if(currentShop!=null && currentShop.getShopId()>0){
            list=productCategoryService.getProductCategoryList(currentShop.getShopId());
            return new Result<List<ProductCategory>>(true,list);
        }else{
            ProductCategoryStateEnum ps=ProductCategoryStateEnum.INNER_ERROR;
            return new Result<List<ProductCategory>>(false,ps.getState(),ps.getStateInfo());
        }
    }

    @RequestMapping(value = "/addproductcategorys",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> batchAddProductCategory(@RequestBody List<ProductCategory> productCategoryList,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        for(ProductCategory productCategory:productCategoryList){
            productCategory.setShopId(currentShop.getShopId());
        }
        if(productCategoryList!=null && productCategoryList.size()>0){
            try{
                ProductCategoryExecution pe=productCategoryService.batchAddProductCategory(productCategoryList);
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少输入一个商品类别");
        }
       return modelMap;
    }

    @RequestMapping(value = "removeproductcategory",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> removeProductCategory(Long productCategoryId,HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        if(productCategoryId !=null && productCategoryId>0){
            Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
            try{
                ProductCategoryExecution pe=productCategoryService.deleteProductCategory(productCategoryId,currentShop.getShopId());
                if(pe.getState()==ProductCategoryStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductCategoryOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请至少选择一个商品类别");
        }
        return modelMap;
    }

}
