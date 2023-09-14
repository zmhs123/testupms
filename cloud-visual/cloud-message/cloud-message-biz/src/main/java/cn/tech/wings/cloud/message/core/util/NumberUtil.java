package cn.tech.wings.cloud.message.core.util;

import java.util.Random;

/**
 * @author xinglx 
 * @Title: mall-cloud
 * @Package com.wing.mall.cloud.order.core.util
 * @Description:数字工具类
 * @date 2019/10/23 13:55
 */
public class NumberUtil {

    /**
     * 生成5位小数
     * @param input
     * @return
     */
    public static String genCode(int input) {
        int num = 100000;
        if (input < num) {
            String codeStr = String.valueOf(input);
            int len = 5 - codeStr.length();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < len; i++) {
                sb.append('0');
            }
            sb.append(codeStr);
            return sb.toString();
        }
        return  input + "" ;
    }

    /**
     * 生成3位随机数
     * @return
     */
    public static String genThreeNum() {
        Random rad = new Random();
        return String.valueOf(rad.nextInt(1000));
    }

    public static void main(String[] args) {
        System.out.println(genCode(5));
        System.out.println(genThreeNum());
    }
}
