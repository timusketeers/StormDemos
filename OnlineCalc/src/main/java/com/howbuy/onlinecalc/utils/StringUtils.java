package com.howbuy.onlinecalc.utils;

public class StringUtils
{
    /**
     * ÅÐ¶Ï×Ö·û´®ÊÇ·ñÎª¿Õ.
     * @param str
     * @return
     */
    public static boolean isNotBlank(String str)
    {
        boolean flag = false;
        if (null != str && !"".equals(str.trim()))
        {
            flag = true;
        }
        
        return flag;
    }

}
