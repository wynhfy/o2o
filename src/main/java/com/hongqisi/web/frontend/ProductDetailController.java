package com.hongqisi.web.frontend;

import com.hongqisi.entity.Product;
import com.hongqisi.service.ProductService;
import com.hongqisi.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/frontend")
public class ProductDetailController {

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/listproductpageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listProductPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        Product product=null;
        long productId= HttpServletRequestUtil.getLong(request,"productId");
        if(productId!=-1){
            product=productService.getProductById(productId);
            modelMap.put("product",product);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg:","empty productId");
        }
        return modelMap;
    }

}
