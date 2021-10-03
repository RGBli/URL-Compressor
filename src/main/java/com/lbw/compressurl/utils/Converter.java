package com.lbw.compressurl.utils;

public class Converter {
    // 62进制符号表
    private static final char[] table = new char[62];

    // 静态方法块，用于初始化符号表，62进制是 0-9 A-Z a-z
    static {
        int index = 0;
        while (index < 10) {
            table[index] = (char) (index + '0');
            index++;
        }
        while (index < 36) {
            table[index] = (char) (index + 55);
            index++;
        }
        while (index < 62) {
            table[index] = (char) (index + 61);
            index++;
        }
    }

    /**
     * 将递增序号转为62进制字符串
     */
    public static String convertSequenceToBase62(Long n) throws Exception {
        if (n == Long.MAX_VALUE) {
            throw new Exception("短链接池已耗尽");
        }
        StringBuilder sb = new StringBuilder();
        while (n != 0) {
            sb.append(table[(int) (n % 62)]);
            n /= 62;
        }
        return sb.reverse().toString();
    }

    /**
     * 测试10进制转62进制算法是否正确
     */
    public static void main(String[] args) throws Exception {
        System.out.println(convertSequenceToBase62(124389345L));
    }
}
