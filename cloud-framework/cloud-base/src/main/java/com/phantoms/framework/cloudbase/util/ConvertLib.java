package com.phantoms.framework.cloudbase.util;

import java.math.BigDecimal;

/**
 * Object转基础类型lib
 */
public class ConvertLib {

    public static boolean toBoolean(Object value){
        if(value != null){
            if(value instanceof Boolean){
                return (Boolean)value;
            }
            try{
                return Boolean.valueOf(value.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;
    }

    public static double toDouble(Object value){
        if(value != null){
            if(value instanceof Double){
                return (Double)value;
            }
            try{
                return Double.valueOf(value.toString());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return 0;
    }

    public static int toInt(Object value){
        if(value != null){
            if(value instanceof Integer){
                return (Integer)value;
            }
            if(value.toString().matches("-?\\d+")) {
                try {
                    return Integer.valueOf(value.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static long toLong(Object value){
        if(value != null){
            if(value instanceof Long){
                return (Long)value;
            }
            if(value.toString().matches("-?\\d+")){
                try{
                    return Long.valueOf(value.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static double roundHalfUp(double value, int scale){
        BigDecimal b = new BigDecimal(value);
        return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    public static double roundDown(double value, int scale){
        BigDecimal b = new BigDecimal(value);
        return b.setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
    }
 
}
