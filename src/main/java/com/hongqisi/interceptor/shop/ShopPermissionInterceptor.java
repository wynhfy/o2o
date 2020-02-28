package com.hongqisi.interceptor.shop;

import com.hongqisi.entity.Shop;
import java.util.List;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ShopPermissionInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从session中获取当前选择的店铺
        Shop currentShop=(Shop)request.getSession().getAttribute("currentShop");
        //从session中获取当前用户可操作的店铺列表
        @SuppressWarnings("unchecked")
        List<Shop> shopList=(List<Shop>)request.getSession().getAttribute("shopList");
        if(currentShop!=null&&shopList!=null){
            for(Shop shop:shopList){
                if(currentShop.getShopId()==shop.getShopId()){
                    return true;
                }
            }
        }
        return false;
    }
}
