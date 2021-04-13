package com.lbw.compressurl.utils;

public class Converter {
    // 62进制符号表
    private static final char[] table = new char[62];

    // 静态方法块，用于初始化符号表
    static {
        int index = 0;
        while (index < 10) {
            table[index] = (char)(index + '0');
            index++;
        }
        while (index < 36) {
            table[index] = (char)(index + 55);
            index++;
        }
        while (index < 62) {
            table[index] = (char)(index + 61);
            index++;
        }
    }

    /**
     * 将递增序号转为5位的62进制字符串
     * 选用5位的原因是 62^5=916132832，在保证短链接足够短的条件下可以存放9亿个长链接
     * 62进制是 0-9 A-Z a-z
     */
    public static String convertSequenceToBase62(int n) throws Exception {
        if (n > 916132832) {
            throw new Exception("短链接已用完");
        }
        StringBuilder sb = new StringBuilder();
        while (n != 0) {
            sb.append(table[n % 62]);
            n /= 62;
        }
        int len = sb.length();
        for (int i = 0; i < 5 - len; i++) {
            sb.append(0);
        }
        return sb.reverse().toString();
    }

    /**
     * 测试10进制转62进制算法是否正确
     */
    public static void main(String[] args) throws Exception {
        System.out.println(convertSequenceToBase62(1));
    }
}
