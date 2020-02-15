package com.hongqisi.web.shopadmin;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.dto.ShopExecution;
import com.hongqisi.entity.Area;
import com.hongqisi.entity.PersonInfo;
import com.hongqisi.entity.Shop;
import com.hongqisi.entity.ShopCategory;
import com.hongqisi.enums.ShopStateEnum;
import com.hongqisi.service.AreaService;
import com.hongqisi.service.ShopCategoryService;
import com.hongqisi.service.ShopService;
import com.hongqisi.util.HttpServletRequestUtil;
import com.hongqisi.util.ImageUtil;
import com.hongqisi.util.PathUtil;
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
        String shopstr= HttpServletRequestUtil.getString(request,"shopstr");
        ObjectMapper mapper=new ObjectMapper();
        Shop shop=null;
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
            //session todo
            PersonInfo owner=new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            ShopExecution se= null;
            try {
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if(se.getState()== ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
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
