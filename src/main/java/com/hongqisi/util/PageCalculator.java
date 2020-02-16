package com.hongqisi.util;

public class PageCalculator {

    /**
     * 将页数转换为行数
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public static int calculateRowIndex(int pageIndex,int pageSize){
        return (pageIndex>0) ? (pageIndex-1)*pageSize :0;
    }

}
