package org.femtoframework.util;



import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;


/**
 * 用于测试的实例Bean
 *
 * @author fengyun
 * @version 1.00 2004-3-19 13:35:56
 */
public class NutletBean implements Cloneable, Serializable
{
    public String field1;
    public int field2;
    public byte field3;
    public char field4;
    public long field5;
    public float field6;
    public double field7;
    public boolean field8;


    public String[] field9;
    public int[] field10;
    public byte[] field11;
    public char[] field12;
    public long[] field13;
    public float[] field14;
    public double[] field15;
    public boolean[] field16;

    private static transient Random random = new Random();

    public NutletBean()
    {
        field1 = String.valueOf(random.nextLong());
        field2 = random.nextInt();
        field3 = (byte)random.nextInt();
        field4 = (char)random.nextInt();
        field5 = random.nextLong();
        field6 = random.nextFloat();
        field7 = random.nextDouble();
        field8 = random.nextBoolean();

        field9 = new String[random.nextInt(10) + 2];
        for (int i = 0; i < field9.length; i++) {
            field9[i] = String.valueOf(random.nextLong());
        }

        field10 = new int[random.nextInt(10) + 2];
        for (int i = 0; i < field10.length; i++) {
            field10[i] = random.nextInt();
        }

        field11 = new byte[random.nextInt(10) + 2];
        for (int i = 0; i < field11.length; i++) {
            field11[i] = (byte)random.nextInt();
        }

        field12 = new char[random.nextInt(10) + 2];
        for (int i = 0; i < field12.length; i++) {
            field12[i] = (char)random.nextInt();
        }

        field13 = new long[random.nextInt(10) + 2];
        for (int i = 0; i < field13.length; i++) {
            field13[i] = random.nextLong();
        }

        field14 = new float[random.nextInt(10) + 2];
        for (int i = 0; i < field14.length; i++) {
            field14[i] = random.nextFloat();
        }

        field15 = new double[random.nextInt(10) + 2];
        for (int i = 0; i < field15.length; i++) {
            field15[i] = random.nextDouble();
        }

        field16 = new boolean[random.nextInt(10) + 2];
        for (int i = 0; i < field16.length; i++) {
            field16[i] = random.nextBoolean();
        }
    }

    /**
     * Clone
     *
     * @return
     */
    public Object clone() throws CloneNotSupportedException
    {
        NutletBean bean = (NutletBean)super.clone();
        bean.field1 = field1;
        bean.field9 = field9;
        bean.field10 = field10;
        bean.field11 = field11;
        bean.field12 = field12;
        bean.field13 = field13;
        bean.field14 = field14;
        bean.field15 = field15;
        bean.field16 = field16;
        return bean;
    }


    /**
     * 判断两个对象是否相等
     *
     * @param obj 对象
     * @return
     */
    public boolean equals(Object obj)
    {
        if (obj == this) {
            return true;
        }
        if (obj instanceof NutletBean) {
            NutletBean bean = (NutletBean)obj;
            if (!field1.equals(bean.field1)) {
                return false;
            }
            if (field2 != bean.field2) {
                return false;
            }
            if (field3 != bean.field3) {
                return false;
            }
            if (field4 != bean.field4) {
                return false;
            }
            if (field5 != bean.field5) {
                return false;
            }
            if (field6 != bean.field6) {
                return false;
            }
            if (field7 != bean.field7) {
                return false;
            }
            if (field8 != bean.field8) {
                return false;
            }
            if (!Arrays.equals(field9, bean.field9)) {
                return false;
            }
            if (!Arrays.equals(field10, bean.field10)) {
                return false;
            }
            if (!Arrays.equals(field11, bean.field11)) {
                return false;
            }
            if (!Arrays.equals(field12, bean.field12)) {
                return false;
            }
            if (!Arrays.equals(field13, bean.field13)) {
                return false;
            }
            if (!Arrays.equals(field14, bean.field14)) {
                return false;
            }
            if (!Arrays.equals(field15, bean.field15)) {
                return false;
            }
            return true;
        }
        return false;
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder(64);
        sb.append("Field1=").append(field1).append(';');
        sb.append("Field2=").append(field2).append(';');
        sb.append("Field3=").append(field3).append(';');
        sb.append("Field4=").append(field4).append(';');
        sb.append("Field5=").append(field5).append(';');
        sb.append("Field6=").append(field6).append(';');
        sb.append("Field7=").append(field7).append(';');
        sb.append("Field8=").append(field8).append(';');
//        sb.append("Field9=").append(StringUtil.toString(field9, ',')).append(';');
//        sb.append("Field10=").append(StringUtil.toString(field10, ',')).append(';');
//        sb.append("Field11=").append(StringUtil.toString(field11, ',')).append(';');
//        sb.append("Field12=").append(StringUtil.toString(field12, ',')).append(';');
//        sb.append("Field13=").append(StringUtil.toString(field13, ',')).append(';');
//        sb.append("Field14=").append(StringUtil.toString(field14, ',', 2)).append(';');
//        sb.append("Field15=").append(StringUtil.toString(field15, ',', 2));
//        sb.append("Field16=").append(StringUtil.toString(field16, ','));

        return sb.toString();
    }
}
