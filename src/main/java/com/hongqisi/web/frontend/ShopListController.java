package com.hongqisi.web.frontend;

import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Area;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.service.AreaService;
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
public class ShopListController {

    @Autowired
    private ShopCategoryService shopCategoryService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopService shopService;


    /**
     * 返回商品列表页里的ShopCategory列表，（第一级或者第二级），以及区域信息列表
     * 这是在首页点进去之后看到的
     * @param request
     * @return
     */
    @RequestMapping(value = "/listshopspageinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShopsPageInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        long parentId= HttpServletRequestUtil.getLong(request,"parentId");
        List<ShopCategory> shopCategoryList=null;
        if(parentId!=-1){
            try{
                //如果parentId存在，则取出该一级shopcategory下的二级shopcategory列表
                ShopCategory shopCategoryCondition=new ShopCategory();
                ShopCategory parent=new ShopCategory();
                parent.setShopCategoryId(parentId);
                shopCategoryCondition.setParent(parent);
                shopCategoryList=shopCategoryService.getShopCategoryList(shopCategoryCondition);
            }catch(Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg:",e.getMessage());
            }
        }else{
            //如果parentId不存在，则取出所有一级shopcategoryId(用户在首页选择的是全部商品列表)
            try{
                shopCategoryList=shopCategoryService.getShopCategoryList(null);
            }catch (Exception e){
                modelMap.put("success",false);
                modelMap.put("errMsg:",e.getMessage());
            }
        }
        modelMap.put("shopCategoryList",shopCategoryList);
        List<Area> areaList=null;
        try{
            areaList=areaService.getAreaList();
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
            return modelMap;
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg:",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 获取指定查询条件下的店铺列表
     * 首页点进去之后，在点查询看到的
     */
    @RequestMapping(value = "/listshops",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> listShops(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
        if(pageIndex>-1 && pageSize>-1){
            long parentId=HttpServletRequestUtil.getLong(request,"parentId");
            long shopCategoryId=HttpServletRequestUtil.getLong(request,"shopCategoryId");
            int areaId=HttpServletRequestUtil.getInt(request,"areaId");
            String shopName=HttpServletRequestUtil.getString(request,"shopName");
            Shop shopCondition=compactShopCondition4Search(parentId,shopCategoryId,areaId,shopName);
            ShopExecution se=shopService.getShopList(shopCondition,pageIndex,pageSize);
            modelMap.put("shopList",se.getShopList());
            modelMap.put("count",se.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg:","empty pageIndex or pageSize");
        }
        return modelMap;
    }


    private Shop compactShopCondition4Search(long parentId, long shopCategoryId, int areaId, String shopName) {
        Shop shopCondition = new Shop();
        if (parentId != -1L) {
            // 查询某个一级ShopCategory下面的所有二级ShopCategory里面的店铺列表
            ShopCategory childCategory = new ShopCategory();
            ShopCategory parentCategory = new ShopCategory();
            parentCategory.setShopCategoryId(parentId);
            childCategory.setParent(parentCategory);
            shopCondition.setShopCategory(childCategory);
        }
        if (shopCategoryId != -1L) {
            // 查询某个二级ShopCategory下面的店铺列表
            ShopCategory shopCategory = new ShopCategory();
            shopCategory.setShopCategoryId(shopCategoryId);
            shopCondition.setShopCategory(shopCategory);
        }
        if (areaId != -1L) {
            // 查询位于某个区域Id下的店铺列表
            Area area = new Area();
            area.setAreaId(areaId);
            shopCondition.setArea(area);
        }

        if (shopName != null) {
            // 查询名字里包含shopName的店铺列表
            shopCondition.setShopName(shopName);
        }
        // 前端展示的店铺都是审核成功的店铺
        shopCondition.setEnableStatus(1);
        return shopCondition;
    }


}
