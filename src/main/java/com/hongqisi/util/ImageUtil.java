package com.hongqisi.util;

import com.hongqisi.dto.ImageHolder;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Position;
import net.coobird.thumbnailator.geometry.Positions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 图片处理工具
 */
public class ImageUtil {

    private static Logger  logger= LoggerFactory.getLogger(ImageUtil.class);

    //获取类路径
    private static String basepath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
    private static final SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r=new Random();

    /**
     * 将CommonsMultipartFile转换成File类
     * @param cfile
     * @return
     */
    public static File transferCommonsMutipartFileToFile(CommonsMultipartFile cfile){
        File newFile=new File(cfile.getOriginalFilename());
        try {
            cfile.transferTo(newFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newFile;
    }

    /**
     * 处理用户传送过来的对象，处理缩略图并生成图片的相对路径
     * @param thumbnail  spring自带的文件处理对象，转换成了File
     * @param targetAddr 图片的目标存储路径
     * @return
     */
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr){
        //由于用户传过来的图片很容易重名，所以要编写一个随机名字
        String realFileName=getRandomFileName();
        //获取图片扩展名
        String extension=getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension; //得到相对路径
        logger.debug("current relativeAddr is:"+relativeAddr);
        File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
        logger.debug("current completeAddr is:"+PathUtil.getImgBasePath()+relativeAddr);
        //生成缩略图
        try{
            //System.out.println(basepath);
            Thumbnails.of(thumbnail.getImage()).size(200,200)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basepath+"watermark.jpg")),0.25f)
                    .outputQuality(0.8f).toFile(dest);
        } catch (IOException e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 处理商品详情图
     * @param thumbnail
     * @param targetAddr
     * @return
     */
    public static String generateNormalImg(ImageHolder thumbnail,String targetAddr){
        String realFileName=ImageUtil.getRandomFileName();
        String extension= ImageUtil.getFileExtension(thumbnail.getImageName());
        makeDirPath(targetAddr);
        String relativeAddr=targetAddr+realFileName+extension;
        File dest=new File(PathUtil.getImgBasePath()+relativeAddr);
        try{
            Thumbnails.of(thumbnail.getImage()).size(337,640)
                    .watermark(Positions.BOTTOM_RIGHT,ImageIO.read(new File(basepath+"watermark.jpg")),0.25f)
                    .outputQuality(0.9f).toFile(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return relativeAddr;
    }

    /**
     * 创建目标路径所涉及到的目录，即/home/work/wyn/xxx.jpg
     * 那么 home work wyn三个目录都得自动创建出来
     * @param targetAddr
     */
    private static void makeDirPath(String targetAddr) {
        String realFileParentPath=PathUtil.getImgBasePath()+targetAddr;
        File dirPath=new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }

    /**
     * 生成随机文件名，当前年月日小时分钟秒数+五位随机数
     * @return
     */
    public static String getRandomFileName(){
        //获取随机五位数
        int rannum=r.nextInt(89999)+10000;
        String nowtimestr=sdf.format(new Date());
        return nowtimestr+rannum;
    }

    /**
     * 获取输入文件流的扩展名
     * @param filename
     * @return
     */
    private static String getFileExtension(String filename){
        return filename.substring(filename.lastIndexOf("."));
    }


    /**
     * storePath是文件的路径或者目录的路径
     * 如果storePath是文件路径，则删除该文件
     * 如果storePath是目录路径，则删除该目录下的所有文件
     * @param storePath
     */
    public static void deleteFileOrPath(String storePath){
         File file=new File(PathUtil.getImgBasePath()+storePath);
         if(file.exists()){
             if(file.isDirectory()){
                 File[] files=file.listFiles();
                 for(int i=0;i<files.length;i++) {
                     files[i].delete();
                 }
             }
             file.delete();
         }
    }


    public static void main(String[] args) throws IOException {
        String basepath=Thread.currentThread().getContextClassLoader().getResource("").getPath();
        Thumbnails.of(new File("/Users/ynwu/Pictures/qiaoba.jpg")).size(200,200)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(basepath+"/watermark.jpg")),0.25f)
                .outputQuality(0.8f).toFile("/Users/ynwu/Pictures/qiaobanew.jpg");
    }

}
