package org.femtoframework.util.crypto;



import java.nio.charset.Charset;

/**
 * MD5算法实现
 *
 * @author fengyun
 * @version 1.00 Aug 29, 2002 10:29:20 AM
 */
public class MD5 {
    /**
     * 128-byte state
     */
    private int[] state;

    /**
     * 64-bit character count
     */
    private int[] count;

    /**
     * 64-byte buffer (512 bits) for storing to-be-hashed characters
     */
    private byte[] buffer;

    /**
     * 位结果
     */
    private byte[] bits;

    private static final int[] ZERO = new int[]{0, 0};
    private static final int[] INIT_STATE = new int[]{
        0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};

    /**
     * 对数据进行MD5加密(字符串以"DefaultCharset"形式)
     *
     * @param source 源
     * @return 结果
     */
    public static byte[] encrypt(String source) {
        return encrypt(source, Charset.defaultCharset());
    }

    /**
     * 对数据进行MD5加密
     *
     * @param source   源
     * @param encoding 字符串编码方式
     * @return 结果，如果编码错误返回<code>null</code>
     */
    public static byte[] encrypt(String source, Charset encoding) {
        return encrypt(source.getBytes(encoding));
    }

    private static final byte[] PADDING
        = {-128, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0};

    /**
     * 对数据进行MD5加密
     *
     * @param bytes 源
     * @return 结果
     */
    public static byte[] encrypt(byte[] bytes) {
        return encrypt(bytes, 0, bytes.length);
    }

    /**
     * 对数据进行MD5加密
     *
     * @param bytes 源
     * @return 结果
     */
    public static byte[] encrypt(byte[] bytes, int off, int len) {
        MD5 md5 = new MD5();
        md5.update(bytes, off, len);
        return md5.digest();
    }

    /**
     * 对数据进行MD5加密
     *
     * @param bytes 源
     */
    public static void encrypt(byte[] bytes, byte[] result) {
        encrypt(bytes, 0, bytes.length, result);
    }

    /**
     * 对数据进行MD5加密
     *
     * @param bytes 源
     */
    public static void encrypt(byte[] bytes, int off, int len, byte[] result) {
        if (result.length < 16) {
            throw new IllegalArgumentException("The length of result array must be larger than 16");
        }
        MD5 md5 = new MD5();
        md5.update(bytes, off, len);
        md5.finished(result);
    }

    public MD5() {
        buffer = new byte[64];
        count = new int[2];
        state = new int[]{0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476};
        bits = new byte[8];
    }

    /**
     * 重新恢复
     */
    public void reset() {
        System.arraycopy(ZERO, 0, count, 0, 2);
        System.arraycopy(INIT_STATE, 0, state, 0, 4);
    }

    /**
     * Update
     *
     * @param bytes 数据
     */
    public void update(byte[] bytes) {
        update(bytes, 0, bytes.length);
    }

    /**
     * 结束返回结果
     *
     * @return 结束返回结果
     */
    public byte[] digest() {
        byte[] digest = new byte[16];
        finished(digest);
        return digest;
    }

    public void update(byte[] bytes, int off, int length) {
        int index = (count[0] >>> 3) & 0x3F;
        // /* Update number of bits */
        if ((count[0] += (length << 3)) < (length << 3)) {
            count[1]++;
        }
        count[1] += (length >>> 29);

        int partLen = 64 - index;

        // Transform as many times as possible.
        if (length >= partLen) {
            int[] dec = new int[16];
            int i = off;
            System.arraycopy(bytes, i, buffer, index, partLen);
            transform(buffer, state, dec);
            index = 0;
            int end = off + length;
            //byte[] block = new byte[64];
            for (i += partLen; i + 63 < end; i += 64) {
                System.arraycopy(bytes, i, buffer, 0, 64);
                transform(buffer, state, dec);
            }
            System.arraycopy(bytes, i, buffer, index, end - i);
        }
        else {
            System.arraycopy(bytes, off, buffer, index, length);
        }
    }

    private void update(byte[] bytes, int length) {
        update(bytes, 0, length);
    }

    /*
      md5Final整理和填写输出结果
    */
    private void finished(byte[] result) {
        ///* Save number of bits */
        encode(count, bits, 8);
        ///* Pad out to 56 mod 64.
        int index = (count[0] >>> 3) & 0x3F;
        int padLen = (index < 56) ? (56 - index) : (120 - index);
        update(PADDING, padLen);
        ///* Append length (before padding) */
        update(bits, 8);
        ///* Store state in digest */
        encode(state, result, 16);
    }


    /*
       md5Transform是MD5核心变换程序，有md5Update调用，block是分块的原始字节
    */
    private static final void transform(byte block[],
                                        int[] state,
                                        int[] dec) {
        int a = state[0], b = state[1], c = state[2], d = state[3];
        int[] x = dec;

        decode(block, x, 64);

        /* Round 1 */
        a += ((b & c) | (~b & d)) + x[0] + 0xd76aa478; /* 1 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[1] + 0xe8c7b756; /* 2 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[2] + 0x242070db; /* 3 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[3] + 0xc1bdceee; /* 4 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[4] + 0xf57c0faf; /* 5 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[5] + 0x4787c62a; /* 6 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[6] + 0xa8304613; /* 7 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[7] + 0xfd469501; /* 8 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[8] + 0x698098d8; /* 9 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[9] + 0x8b44f7af; /* 10 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[10] + 0xffff5bb1; /* 11 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[11] + 0x895cd7be; /* 12 */
        b = ((b << 22) | (b >>> 10)) + c;

        a += ((b & c) | (~b & d)) + x[12] + 0x6b901122; /* 13 */
        a = ((a << 7) | (a >>> 25)) + b;
        d += ((a & b) | (~a & c)) + x[13] + 0xfd987193; /* 14 */
        d = ((d << 12) | (d >>> 20)) + a;
        c += ((d & a) | (~d & b)) + x[14] + 0xa679438e; /* 15 */
        c = ((c << 17) | (c >>> 15)) + d;
        b += ((c & d) | (~c & a)) + x[15] + 0x49b40821; /* 16 */
        b = ((b << 22) | (b >>> 10)) + c;

        /* Round 2 */
        a += ((b & d) | (c & ~d)) + x[1] + 0xf61e2562; /* 17 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[6] + 0xc040b340; /* 18 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[11] + 0x265e5a51; /* 19 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[0] + 0xe9b6c7aa; /* 20 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[5] + 0xd62f105d; /* 21 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[10] + 0x02441453; /* 22 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[15] + 0xd8a1e681; /* 23 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[4] + 0xe7d3fbc8; /* 24 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[9] + 0x21e1cde6; /* 25 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[14] + 0xc33707d6; /* 26 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[3] + 0xf4d50d87; /* 27 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[8] + 0x455a14ed; /* 28 */
        b = ((b << 20) | (b >>> 12)) + c;

        a += ((b & d) | (c & ~d)) + x[13] + 0xa9e3e905; /* 29 */
        a = ((a << 5) | (a >>> 27)) + b;
        d += ((a & c) | (b & ~c)) + x[2] + 0xfcefa3f8; /* 30 */
        d = ((d << 9) | (d >>> 23)) + a;
        c += ((d & b) | (a & ~b)) + x[7] + 0x676f02d9; /* 31 */
        c = ((c << 14) | (c >>> 18)) + d;
        b += ((c & a) | (d & ~a)) + x[12] + 0x8d2a4c8a; /* 32 */
        b = ((b << 20) | (b >>> 12)) + c;

        /* Round 3 */
        a += (b ^ c ^ d) + x[5] + 0xfffa3942;      /* 33 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[8] + 0x8771f681;      /* 34 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[11] + 0x6d9d6122;      /* 35 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[14] + 0xfde5380c;      /* 36 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[1] + 0xa4beea44;      /* 37 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[4] + 0x4bdecfa9;      /* 38 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[7] + 0xf6bb4b60;      /* 39 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[10] + 0xbebfbc70;      /* 40 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[13] + 0x289b7ec6;      /* 41 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[0] + 0xeaa127fa;      /* 42 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[3] + 0xd4ef3085;      /* 43 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[6] + 0x04881d05;      /* 44 */
        b = ((b << 23) | (b >>> 9)) + c;

        a += (b ^ c ^ d) + x[9] + 0xd9d4d039;      /* 33 */
        a = ((a << 4) | (a >>> 28)) + b;
        d += (a ^ b ^ c) + x[12] + 0xe6db99e5;      /* 34 */
        d = ((d << 11) | (d >>> 21)) + a;
        c += (d ^ a ^ b) + x[15] + 0x1fa27cf8;      /* 35 */
        c = ((c << 16) | (c >>> 16)) + d;
        b += (c ^ d ^ a) + x[2] + 0xc4ac5665;      /* 36 */
        b = ((b << 23) | (b >>> 9)) + c;

        /* Round 4 */
        a += (c ^ (b | ~d)) + x[0] + 0xf4292244; /* 49 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[7] + 0x432aff97; /* 50 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[14] + 0xab9423a7; /* 51 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[5] + 0xfc93a039; /* 52 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[12] + 0x655b59c3; /* 53 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[3] + 0x8f0ccc92; /* 54 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[10] + 0xffeff47d; /* 55 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[1] + 0x85845dd1; /* 56 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[8] + 0x6fa87e4f; /* 57 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[15] + 0xfe2ce6e0; /* 58 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[6] + 0xa3014314; /* 59 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[13] + 0x4e0811a1; /* 60 */
        b = ((b << 21) | (b >>> 11)) + c;

        a += (c ^ (b | ~d)) + x[4] + 0xf7537e82; /* 61 */
        a = ((a << 6) | (a >>> 26)) + b;
        d += (b ^ (a | ~c)) + x[11] + 0xbd3af235; /* 62 */
        d = ((d << 10) | (d >>> 22)) + a;
        c += (a ^ (d | ~b)) + x[2] + 0x2ad7d2bb; /* 63 */
        c = ((c << 15) | (c >>> 17)) + d;
        b += (d ^ (c | ~a)) + x[9] + 0xeb86d391; /* 64 */
        b = ((b << 21) | (b >>> 11)) + c;

        state[0] += a;
        state[1] += b;
        state[2] += c;
        state[3] += d;
    }

    /**
     * Encode把int数组按顺序拆成byte数组
     */
    private static void encode(int[] input,
                                     byte[] output,
                                     int len) {
        for (int i = 0, j = 0; j < len; i++) {
            output[j++] = (byte)(input[i] & 0xFF);
            output[j++] = (byte)((input[i] >>> 8) & 0xFF);
            output[j++] = (byte)((input[i] >>> 16) & 0xFF);
            output[j++] = (byte)((input[i] >>> 24) & 0xFF);
        }
    }

    /**
     * Decode把byte数组按顺序合成成int数组
     */
    private static void decode(byte[] input,
                                     int[] output,
                                     int len) {
/*
        int ch1, ch2, ch3, ch4;
        for (int i = 0, j = 0; j < len; i++) {
            ch1 = (input[j++] & 0XFF) << 0;
            ch2 = (input[j++] & 0XFF) << 8;
            ch3 = (input[j++] & 0XFF) << 16;
            ch4 = (input[j++] & 0XFF) << 24;
            output[i] = ch1 | ch2 | ch3 | ch4;
        }
*/
//        速度快 20% 可是感觉很难看
        output[0] = ((int)(input[0] & 0xff)) |
                    (((int)(input[1] & 0xff)) << 8) |
                    (((int)(input[2] & 0xff)) << 16) |
                    (((int)input[3]) << 24);
        output[1] = ((int)(input[4] & 0xff)) |
                    (((int)(input[5] & 0xff)) << 8) |
                    (((int)(input[6] & 0xff)) << 16) |
                    (((int)input[7]) << 24);
        output[2] = ((int)(input[8] & 0xff)) |
                    (((int)(input[9] & 0xff)) << 8) |
                    (((int)(input[10] & 0xff)) << 16) |
                    (((int)input[11]) << 24);
        output[3] = ((int)(input[12] & 0xff)) |
                    (((int)(input[13] & 0xff)) << 8) |
                    (((int)(input[14] & 0xff)) << 16) |
                    (((int)input[15]) << 24);
        output[4] = ((int)(input[16] & 0xff)) |
                    (((int)(input[17] & 0xff)) << 8) |
                    (((int)(input[18] & 0xff)) << 16) |
                    (((int)input[19]) << 24);
        output[5] = ((int)(input[20] & 0xff)) |
                    (((int)(input[21] & 0xff)) << 8) |
                    (((int)(input[22] & 0xff)) << 16) |
                    (((int)input[23]) << 24);
        output[6] = ((int)(input[24] & 0xff)) |
                    (((int)(input[25] & 0xff)) << 8) |
                    (((int)(input[26] & 0xff)) << 16) |
                    (((int)input[27]) << 24);
        output[7] = ((int)(input[28] & 0xff)) |
                    (((int)(input[29] & 0xff)) << 8) |
                    (((int)(input[30] & 0xff)) << 16) |
                    (((int)input[31]) << 24);
        output[8] = ((int)(input[32] & 0xff)) |
                    (((int)(input[33] & 0xff)) << 8) |
                    (((int)(input[34] & 0xff)) << 16) |
                    (((int)input[35]) << 24);
        output[9] = ((int)(input[36] & 0xff)) |
                    (((int)(input[37] & 0xff)) << 8) |
                    (((int)(input[38] & 0xff)) << 16) |
                    (((int)input[39]) << 24);
        output[10] = ((int)(input[40] & 0xff)) |
                     (((int)(input[41] & 0xff)) << 8) |
                     (((int)(input[42] & 0xff)) << 16) |
                     (((int)input[43]) << 24);
        output[11] = ((int)(input[44] & 0xff)) |
                     (((int)(input[45] & 0xff)) << 8) |
                     (((int)(input[46] & 0xff)) << 16) |
                     (((int)input[47]) << 24);
        output[12] = ((int)(input[48] & 0xff)) |
                     (((int)(input[49] & 0xff)) << 8) |
                     (((int)(input[50] & 0xff)) << 16) |
                     (((int)input[51]) << 24);
        output[13] = ((int)(input[52] & 0xff)) |
                     (((int)(input[53] & 0xff)) << 8) |
                     (((int)(input[54] & 0xff)) << 16) |
                     (((int)input[55]) << 24);
        output[14] = ((int)(input[56] & 0xff)) |
                     (((int)(input[57] & 0xff)) << 8) |
                     (((int)(input[58] & 0xff)) << 16) |
                     (((int)input[59]) << 24);
        output[15] = ((int)(input[60] & 0xff)) |
                     (((int)(input[61] & 0xff)) << 8) |
                     (((int)(input[62] & 0xff)) << 16) |
                     (((int)input[63]) << 24);
    }
}
