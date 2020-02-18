package com.hongqisi.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ProductExecution;
import com.hongqisi.entity.Product;
import com.hongqisi.entity.Shop;
import com.hongqisi.enums.ProductStateEnum;
import com.hongqisi.exceptions.ProductOperationException;
import com.hongqisi.service.ProductService;
import com.hongqisi.util.CodeUtil;
import com.hongqisi.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/shop")
public class ProductManagementController {

    @Autowired
    private ProductService productService;

    private static final int IMAGEMAXCOUNT=6;

    @RequestMapping(value = "/addproduct",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> addShop(HttpServletRequest request) {
        Map<String,Object> modelMap=new HashMap<>();
        if(!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
            return modelMap;
        }
        ObjectMapper mapper=new ObjectMapper();
        Product product=null;
        String productStr= HttpServletRequestUtil.getString(request,"productStr");
        MultipartHttpServletRequest multipartHttpServletRequest=null;
        ImageHolder thumbnail=null;
        List<ImageHolder> productImgList=new ArrayList<>();
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        //获取图片参数
        try{
            if(commonsMultipartResolver.isMultipart(request)){
                multipartHttpServletRequest=(MultipartHttpServletRequest)request;
                CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("thumbnail");
                thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
                for(int i=0;i<IMAGEMAXCOUNT;i++){
                    CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("productImg"+i);
                    if(productImgFile!=null){
                        ImageHolder imageHolder=new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
                        productImgList.add(imageHolder);
                    }else{
                        break;
                    }
                }
            }else{
                modelMap.put("success",false);
                modelMap.put("errMsg","上传图片不能为空");
                return modelMap;
            }
        }catch(Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        //获取商品参数
        try{
            product=mapper.readValue(productStr,Product.class);
        } catch (Exception e) {
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
        if(product!=null&&thumbnail!=null&&productImgList.size()>0){
            Shop currentShop=(Shop) request.getSession().getAttribute("currentShop");
            product.setShop(currentShop);
            try{
                ProductExecution pe=productService.addShop(product,thumbnail,productImgList);
                if(pe.getState()== ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg",pe.getStateInfo());
                }
            }catch (ProductOperationException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage());
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errmsg","请输入商品信息");
        }
        return modelMap;
    }

}
