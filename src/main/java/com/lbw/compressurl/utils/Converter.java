package com.lbw.compressurl.utils;

public class Converter {
    // 62进制符号表
    private static final char[] TABLE = new char[62];

    // 短链接长度
    public static final int LENGTH = 6;

    // 在给定短链接长度条件下允许的最大短链接数量
    public static final long MAX_NUMBER = (long) Math.pow(62, LENGTH) - 1;

    // 静态方法块，用于初始化符号表，62进制是 0-9 A-Z a-z
    static {
        int i = 0;
        while (i < 10) {
            TABLE[i] = (char) (i + '0');
            i++;
        }
        while (i < 36) {
            TABLE[i] = (char) (i + 55);
            i++;
        }
        while (i < 62) {
            TABLE[i] = (char) (i + 61);
            i++;
        }
    }

    /**
     * 将递增序号转为62进制字符串
     */
    public static String convertSequenceToBase62(long n) throws Exception {
        if (n >= MAX_NUMBER) {
            throw new Exception("短链接池已耗尽");
        }

        StringBuilder sb = new StringBuilder();
        while (n != 0) {
            sb.insert(0, TABLE[(int) (n % 62)]);
            n /= 62;
        }
        return fill(sb.toString());
    }

    /**
     * 将短链接填充到指定长度
     */
    private static String fill(String s) {
        StringBuilder sb = new StringBuilder(s);
        for (int n = s.length(); n < LENGTH; n++) {
            sb.insert(0, '0');
        }
        return sb.toString();
    }

    /**
     * 测试10进制转62进制算法是否正确
     */
    public static void main(String[] args) throws Exception {
        System.out.println(convertSequenceToBase62(124389345L));
    }
}
