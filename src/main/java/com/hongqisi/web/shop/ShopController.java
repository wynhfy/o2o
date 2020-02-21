package com.hongqisi.web.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/shop" ,method = RequestMethod.GET)
public class ShopController {


    @RequestMapping("/shopoperation")
    public String shopOperation(){
        return "shop/shopoperation";
    }

    @RequestMapping("/shoplist")
    public String shopList(){
        return "shop/shoplist";
    }

    @RequestMapping("/shopmanagement")
    public String shopManageMent(){
        return "shop/shopmanagement";
    }

    @RequestMapping("/productcategorymanagement")
    public String productCategoryManagement(){
        return "shop/productcategorymanagement";
    }

    @RequestMapping("/productoperation")
    public String productOperation(){
        return "shop/productoperation";
    }

    @RequestMapping("productmanagement")
    public String productManagement(){
        return "shop/productmanagement";
    }

}
