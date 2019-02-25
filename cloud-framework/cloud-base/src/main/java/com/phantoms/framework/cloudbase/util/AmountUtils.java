// ======================================================================
//
//      Copyright (C) 北京国双科技有限公司
//                    http://www.gridsum.com
//
//      保密性声明：此文件属北京国双科技有限公司所有，仅限拥有由国双科技
//      授予了相应权限的人所查看和所修改。如果你没有被国双科技授予相应的
//      权限而得到此文件，请删除此文件。未得国双科技同意，不得查看、修改、
//      散播此文件。
//
//
// ======================================================================

package com.phantoms.framework.cloudbase.util;

import java.math.BigDecimal;

public class AmountUtils {
    /**金额为分的格式 */
    public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static double changeF2Y(String amount) throws Exception{
        if(!amount.matches(CURRENCY_FEN_REGEX)) {
            throw new Exception("金额格式有误");
        }
        return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).doubleValue();
    }

    /**
     * 将元为单位的转换为分 （乘100）
     *
     * @param amount
     * @return
     */
    public static Double changeY2F(Double amount){
        return BigDecimal.valueOf(amount).multiply(new BigDecimal(100)).doubleValue();
    }

    public static void main(String[] args) {
        //        try {
        //            System.out.println("结果："+changeF2Y("-000a00"));
        //        } catch(Exception e){
        //            System.out.println("----------->>>"+e.getMessage());
        ////          return e.getErrorCode();
        //        }
        //      System.out.println("结果："+changeY2F("1.00000000001E10"));

        try {
            System.out.println(AmountUtils.changeF2Y("1322"));
            System.out.println(AmountUtils.changeY2F(2.01));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //        System.out.println(Long.parseLong(AmountUtils.changeY2F("1000000000000000")));
        //        System.out.println(Integer.parseInt(AmountUtils.changeY2F("10000000")));
        //        System.out.println(Integer.MIN_VALUE);
        //        long a = 0;
        //        System.out.println(a);

    }
}
