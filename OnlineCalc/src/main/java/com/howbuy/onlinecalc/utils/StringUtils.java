package com.howbuy.onlinecalc.utils;

public class StringUtils
{
    /**
     * �ж��ַ����Ƿ�Ϊ��.
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
