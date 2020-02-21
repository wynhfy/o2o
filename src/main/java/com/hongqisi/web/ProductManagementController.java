package com.hongqisi.web;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongqisi.dto.ImageHolder;
import com.hongqisi.dto.ProductExecution;
import com.hongqisi.entity.Product;
import com.hongqisi.entity.ProductCategory;
import com.hongqisi.entity.Shop;
import com.hongqisi.enums.ProductStateEnum;
import com.hongqisi.exceptions.ProductOperationException;
import com.hongqisi.service.ProductCategoryService;
import com.hongqisi.service.ProductService;
import com.hongqisi.util.CodeUtil;
import com.hongqisi.util.HttpServletRequestUtil;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    private ProductCategoryService productCategoryService;

    private static final int IMAGEMAXCOUNT=6;


    public Product compactProductCondition(long shopId,int productCategoryId,String produtName){
        Product product=new Product();
        Shop shop=new Shop();
        shop.setShopId(shopId);
        product.setShop(shop);
        if(productCategoryId!=-1L){
            ProductCategory pc=new ProductCategory();
            pc.setProductCategoryId(productCategoryId);
            product.setProductCategory(pc);
        }
        if(produtName!=null){
            product.setProductName(produtName);
        }
        return product;
    }

    @RequestMapping(value = "/getproductlistbyshop",method = RequestMethod.GET)
    @ResponseBody
    public Map<String,Object> getProductList(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        int pageIndex=HttpServletRequestUtil.getInt(request,"pageIndex");
        int pageSize=HttpServletRequestUtil.getInt(request,"pageSize");
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        if(pageIndex>-1 && pageSize>-1 &&currentShop!=null&&currentShop.getShopId()!=null){
            int productCategoryId =HttpServletRequestUtil.getInt(request,"productCategoryId");
            String productName=HttpServletRequestUtil.getString(request,"productName");
            Product productCondition=compactProductCondition(currentShop.getShopId(),productCategoryId,productName);
            ProductExecution pe=productService.getProductList(productCondition,pageIndex,pageSize);
            modelMap.put("productList",pe.getProductList());
            modelMap.put("count",pe.getCount());
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty !");
        }
        return modelMap;
    }

    @RequestMapping(value = "/modifyproduct" ,method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> modifyProduct(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<>();
        //是商品编辑的时候调用还是上下架的时候调用
        //前者要通过验证码，后者不用
        boolean statusChange=HttpServletRequestUtil.getBoolean(request,"statusChange");
        if(!statusChange&&!CodeUtil.checkVerifyCode(request)){
            modelMap.put("success",false);
            modelMap.put("errMsg","验证码输入错误");
            return modelMap;
        }
        //接受前端参数
        ObjectMapper mapper=new ObjectMapper();
        Product product=null;
        ImageHolder thumbnail=null;
        List<ImageHolder> productImgList=new ArrayList<>();
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        try{
            if(multipartResolver.isMultipart(request)){
                //取出缩略图
                thumbnail = handleImage(request, thumbnail, productImgList);
            }
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg:",e.getMessage());
            return  modelMap;
        }
        try{
            String productStr=HttpServletRequestUtil.getString(request,"productStr");
            product=mapper.readValue(productStr,Product.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg:",e.getMessage());
            return  modelMap;
        }
        if(product!=null){
            try{
                Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
                product.setShop(currentShop);
                ProductExecution pe=productService.modifyProduct(product,thumbnail,productImgList);
                if(pe.getState()==ProductStateEnum.SUCCESS.getState()){
                    modelMap.put("success",true);
                }else{
                    modelMap.put("success",false);
                    modelMap.put("errMsg:",pe.getStateInfo());
                }
            }catch (RuntimeException e){
                modelMap.put("success",false);
                modelMap.put("errMsg",e.getMessage()) ;
                return modelMap;
            }
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg:","请输入商品信息");
        }
        return modelMap;
    }

    @RequestMapping(value = "/getproductbyid" ,method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getProductById(@RequestParam Long productId){
        Map<String,Object> modelMap=new HashMap<>();
        if(productId>-1){
            Product product=productService.getProductById(productId);
            List<ProductCategory> productCategoryList=productCategoryService.getProductCategoryList(product.getShop().getShopId());
            modelMap.put("product",product);
            modelMap.put("productCategoryList",productCategoryList);
            modelMap.put("success",true);
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","empty productId");
        }
        return modelMap;
    }

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
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(request.getSession().getServletContext());
        //获取图片参数
        try{
            if(multipartResolver.isMultipart(request)){
                thumbnail = handleImage((MultipartHttpServletRequest) request, thumbnail, productImgList);
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

    public ImageHolder handleImage(HttpServletRequest request, ImageHolder thumbnail, List<ImageHolder> productImgList) throws IOException {
        MultipartHttpServletRequest multipartHttpServletRequest=(MultipartHttpServletRequest)request;
        CommonsMultipartFile thumbnailFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("thumbnail");
        if(thumbnailFile!=null){
            thumbnail=new ImageHolder(thumbnailFile.getOriginalFilename(),thumbnailFile.getInputStream());
        }
        for(int i=0;i<IMAGEMAXCOUNT;i++){
            CommonsMultipartFile productImgFile=(CommonsMultipartFile)multipartHttpServletRequest.getFile("productImg"+i);
            if(productImgFile!=null){
                ImageHolder imageHolder=new ImageHolder(productImgFile.getOriginalFilename(),productImgFile.getInputStream());
                productImgList.add(imageHolder);
            }else{
                break;
            }
        }
        return thumbnail;
    }

}
