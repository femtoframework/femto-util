package org.femtoframework.nio;

import org.femtoframework.io.IOUtil;
import org.femtoframework.util.nutlet.NutletUtil;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.*;

import static org.junit.Assert.fail;

/**
 * NioUtil单元测试
 *
 * @author fengyun
 * @version 1.00 2004-12-27 10:53:44
 */

public class NioUtilTest
{
    /**
     * 从一个Channel拷贝数据到其它Channel（边界）
     *
     * @throws Exception
     */
    @Test
    public void testCopyTo0() throws Exception
    {
        try {
            IOUtil.copyTo(null, null);
            fail("IllegalArgument");
        }
        catch (IllegalArgumentException iae) {
        }
    }

    /**
     * 从一个Channel拷贝数据到其它Channel
     *
     * @throws Exception
     */
    @Test
    public void testCopyTo1() throws Exception
    {
        String fileName = "org/femtoframework/nio/NioUtilTest.class";
        File src = NutletUtil.getResourceAsFile(fileName);
        FileInputStream fis = new FileInputStream(src);
        FileChannel readFrom = fis.getChannel();
        File dest = NutletUtil.createTmpFile(fileName + ".bak");
        FileOutputStream fos = new FileOutputStream(dest);
        FileChannel writeTo = fos.getChannel();
        IOUtil.copyTo(readFrom, writeTo);
        writeTo.force(true);
        IOUtil.close(fis);
        IOUtil.close(fos);
        IOUtil.close(readFrom);
        IOUtil.close(writeTo);

        FileInputStream fis1 = new FileInputStream(src);
        FileInputStream fis2 = new FileInputStream(dest);

        int read = -1;
        while ((read = fis1.read()) == fis2.read()) {
            if (read == -1) {
                break;
            }
        }
        if (!(fis1.read() == -1 && fis2.read() == -1)) {
            fail("The file not same");
        }
        IOUtil.close(fis1);
        IOUtil.close(fis2);
        src.delete();
        dest.delete();
    }
}