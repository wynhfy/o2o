package com.hongqisi.util;

/**
 * 路径处理工具
 */
public class PathUtil {

    private static String separator=System.getProperty("file.separator");

    /**
     * 返回项目图片的根路径
     * @return
     */
    public static String getImgBasePath(){
        String os=System.getProperty("os.name");
        String basepath="";
        if(os.toLowerCase().startsWith("win")){
            basepath="D:/projectdev/image";
        }else{
            basepath="/Users/ynwu/wyn/image/";
        }
        basepath=basepath.replace("/",separator);
        return basepath;
    }

    /**
     * 返回项目图片的子路径
     * @param shopId
     * @return
     */
    public static String getShopImgPath(long shopId){
        String imagePath="upload/item/shop/"+shopId+"/";
        return imagePath.replace("/",separator);
    }

}
