package com.hongqisi.web.frontend;

import com.hongqisi.dao.HeadLineDao;
import com.hongqisi.dao.ShopCategoryDao;
import com.hongqisi.entity.HeadLine;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.service.HeadLineService;
import com.hongqisi.service.ShopCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/frontend")
public class MainPageController {

     @Autowired
     private HeadLineService headLineService;
     @Autowired
     private ShopCategoryService shopCategoryService;


     @RequestMapping(value = "/listmainpageinfo",method = RequestMethod.GET)
     @ResponseBody
     public Map<String,Object> listMainPageInfo(){
         Map<String,Object> modelMap=new HashMap<>();
         List<HeadLine> headLineList=new ArrayList<>();
         List<ShopCategory> shopCategoryList=new ArrayList<>();
         try{
             shopCategoryList=shopCategoryService.getShopCategoryList(null);
             modelMap.put("shopCategoryList",shopCategoryList);
         }catch (Exception e){
             modelMap.put("success",false);
             modelMap.put("errMsg",e.getMessage());
         }
         try{
             HeadLine headLine=new HeadLine();
             headLine.setEnableStatus(1);
             headLineList=headLineService.getHeadLineList(headLine);
             modelMap.put("headLineList",headLineList);
         }catch (Exception e){
             modelMap.put("success",false);
             modelMap.put("errMsg",e.getMessage());
         }
         modelMap.put("success",true);
         return modelMap;
     }

}
