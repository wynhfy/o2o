package com.hongqisi.web.shop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Area;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.enums.ShopStateEnum;
import com.hongqisi.service.AreaService;
import com.hongqisi.service.ShopCategoryService;
import com.hongqisi.service.ShopService;
import com.hongqisi.util.CodeUtil;
import com.hongqisi.util.HttpServletRequestUtil;
import com.hongqisi.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ShopManagementController {


    @Autowired
    private ShopService shopService;
    @Autowired
    private AreaService areaService;
    @Autowired
    private ShopCategoryService shopCategoryService;

    @RequestMapping(value = "/getshopmanagementinfo" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopManageMentInfo(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        long shopId=HttpServletRequestUtil.getLong(request,"shopId");
        if(shopId<=0){
            Object currentShopObj=request.getSession().getAttribute("currentShop");
            if(currentShopObj==null){
                modelMap.put("redirect",true);
                modelMap.put("url","o2o/shop/shoplist");
            }else{
                Shop currentShop=(Shop)currentShopObj;
                modelMap.put("redirect",false);
                modelMap.put("shopId",currentShop.getShopId());
            }
        }else{
            Shop currentShop=new Shop();
            currentShop.setShopId(shopId);
            request.getSession().setAttribute("currentShop",currentShop);
            modelMap.put("redirect",false);
        }
        return modelMap;
    }

    @RequestMapping(value = "/getshoplist", method=RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopList(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        // todo 应该由session来做
//        PersonInfo user=new PersonInfo();
//        user.setUserId(1L);
//        user.setName("洪起司");
//        request.getSession().setAttribute("user",user);
        PersonInfo user=(PersonInfo)request.getSession().getAttribute("user");
        try{
            Shop shopCondition=new Shop();
            shopCondition.setOwner(user);
            ShopExecution se=shopService.getShopList(shopCondition,0,100);
            modelMap.put("shopList",se.getShopList());
            //列出店铺成功之后，将店铺放入session中作为权限验证依据，即该账号只能操作自己的店铺
            request.getSession().setAttribute("shopList",se.getShopList());
            modelMap.put("user",user);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }


    /**
     * 通过id获取Shop
     * @param request
     * @return
     */
    @RequestMapping(value = "/getshopbyid",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopById(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        Long shopId=HttpServletRequestUtil.getLong(request,"shopId");
        try{
            if(shopId>-1){
                Shop shop=shopService.getShopById(shopId);
                List<Area> areaList=areaService.getAreaList();
                modelMap.put("shop",shop);
                modelMap.put("areaList",areaList);
                modelMap.put("success",true);
            }
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg","empty shopId");
        }
        return modelMap;
    }

    /**
     * 初始化店铺页面
     * @return
     */
    @RequestMapping(value = "/getshopinitinfo",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getShopInitInfo(){
        Map<String,Object> modelMap=new HashMap<>();
        List<Area> areaList=new ArrayList<>();
        List<ShopCategory> shopCategoryList=new ArrayList<>();
        try{
            shopCategoryList=shopCategoryService.getShopCategoryList(new ShopCategory());
            areaList=areaService.getAreaList();
            modelMap.put("shopCategoryList",shopCategoryList);
            modelMap.put("areaList",areaList);
            modelMap.put("success",true);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
        }
        return modelMap;
    }
    /**
     * 修改店铺信息
     */
    @RequestMapping(value = "/modifyshop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        //判断验证码
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
            return modelMap;
        }
        //得到参数
        String shopstr=HttpServletRequestUtil.getString(request,"shopstr");
        ObjectMapper mapper=new ObjectMapper();
        Shop shop=null;
        try {
            shop=mapper.readValue(shopstr,Shop.class);
        } catch (IOException e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg=null;
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
            shopImg=(CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }
        //修改店铺信息
        if(shop!=null&&shop.getShopId()!=null){
            ShopExecution se;
            try{
                if(shopImg==null){
                    se=shopService.modifyShop(shop,null);
                }else{
                    ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    se=shopService.modifyShop(shop,imageHolder);
                }
                if(se.getState()==ShopStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateinfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺ID");
            return modelMap;
        }
    }

    /**
     * 1、接受并转换相应的参数，包括店铺信息以及图片信息
     * 2、注册店铺
     * 3、返回结果
     * @param request
     * @return
     */
    @RequestMapping(value="/registershop",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> registerShop(HttpServletRequest request){
        //获取店铺和图片信息
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
            return modelMap;
        }
        String shopstr= HttpServletRequestUtil.getString(request,"shopstr");
        ObjectMapper mapper=new ObjectMapper();
        Shop shop=null;
        //将json数据转换为对象
        try{
            shop=mapper.readValue(shopstr,Shop.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        CommonsMultipartFile shopImg=null;
        //从此次会话的上下文内容中获取
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
            shopImg=(CommonsMultipartFile)multipartHttpServletRequest.getFile("shopImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","上传文件不能为空");
            return modelMap;
        }

        //注册店铺
        if(shop!=null&&shopImg!=null){
            //session TODO
            PersonInfo owner=(PersonInfo)request.getSession().getAttribute("user");
            shop.setOwner(owner);
//            PersonInfo owner=new PersonInfo();
//            owner.setUserId(1L);
//            shop.setOwner(owner);
            ShopExecution se= null;
            try {
                ImageHolder imageHolder=new ImageHolder(shopImg.getOriginalFilename(),shopImg.getInputStream());
                    se = shopService.addShop(shop,imageHolder);
                if(se.getState()== ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                    //该用户可以操作的店铺列表
                    List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
                    if(shopList==null||shopList.size()==0){
                        shopList=new ArrayList<Shop>();
                    }
                    shopList.add(se.getShop());
                    request.getSession().setAttribute("shopList",shopList);

                }else{
                    modelMap.put("success",false);
                    modelMap.put("errorMsg",se.getStateinfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errorMsg",se.getStateinfo());
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","输入店铺信息");
            return modelMap;
        }
    }

//    /**
//     * 将CommonsMultipartFile转换为File
//     * @param ins
//     * @param file
//     */
//    private static void inputStreamToFile(InputStream ins,File file){
//        FileOutputStream os=null;
//        try{
//            os=new FileOutputStream(file);
//            int readBytes=0;
//            byte[] buffer=new byte[1024];
//            while((readBytes=ins.read(buffer))!=-1){
//                os.write(buffer,0,readBytes);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("调用inputstream产生异常"+e.getMessage());
//        }finally {
//            try{
//                if(os!=null){
//                    os.close();
//                }
//                if(ins!=null){
//                    ins.close();
//                }
//            }catch (Exception e){
//                throw new RuntimeException("inputStreamToFile关闭io异常"+e.getMessage());
//            }
//        }
//    }

}
