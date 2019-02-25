package com.phantoms.framework.cloudbase.util;

import java.util.Arrays;

/**
 * 二分法查找，折半算法 <Change to the actual description of this class>
 * 
 * @version <pre>
 * Author	Version		Date		Changes
 * zyj 	1.0  		2018年12月2日 	Created
 *
 * </pre>
 * @since 1.
 */
public class BinarySearch {

   
    /**
     * 查找某个数所在的区间，向下取区间 比如数组 int[] array = {1,6,13,24,35};输入8，那么得到的是index=1,
     * array[index]=6
     * 
     * @param num
     * @return
     */
    public static int searchRegion(int[] array, long num) {
        Arrays.sort(array); // 默认排序
        if (num >= array[array.length - 1]) {
            return array.length - 1;// 默认最后一个
        }
        // 这里去大于等于是因为防止array[0]!=0; 而且下面的算法也会默认返回index=0
        if (array[0] >= num) {
            return 0;
        }
        int index = 0; // 检索的时候
        int start = 0; // 用start和end两个索引控制它的查询范围
        int end = array.length - 1;
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            count++;
            index = (start + end) / 2;
            if (array[index] <= num) {
                start = index;
                if (num < array[index + 1]) {
                    System.out.println("---searchRegion 找到index=" + index + " count=" + count);
                    break;
                }
            } else if (num < array[index]) {
                end = index;
            }
        }
        return index;
    }

    public static int binarySearch(int[] array, int num) {
        int index = 0; // 检索的时候
        int start = 0; // 用start和end两个索引控制它的查询范围
        int end = array.length - 1;
        int count = 0;
        if (array[array.length-1] == num) {
            return array.length-1;
        }
        if (array[0] == num) {
            return 0;
        }
        for (int i = 0; i < array.length; i++) {
            count++;
            index = (start + end) / 2;
            if (index == start) {
                System.out.println("抱歉，没有找到");
                index=-1;
                break;
            } else if (array[index] < num) {
                start = index;
            } else if (array[index] > num) {
                end = index;
            } else {
                System.out.println(array[index] + "找到了，在数组下标为" + index + "的地方,查找了" + count + "次。");
                break;
            }
        }
        return index;
    }

    public static void main(String[] args) {
        int num = 3;
        int[] array = new int[18];
        array[0] = 2;
        for (int i = 0; i < array.length - 1; i++) {
            array[i + 1] = array[i] + 2;
        }
        System.out.println("return index =" + BinarySearch.searchRegion(array, num));
    }

}
