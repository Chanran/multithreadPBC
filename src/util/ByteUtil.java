package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ByteUtil {
    public static byte[] integerToBytes(int integer, int len) {
    if (integer < 0) { throw new IllegalArgumentException("Can not cast negative to bytes : " + integer); }
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        for (int i = 0; i < len; i ++) {
            bo.write(integer);
            integer = integer >> 8;
        }
        return bo.toByteArray();
    }

    public static int bytesToInteger(byte[] bytes){
        int integer = 0;
        int len = bytes.length;
        for (int i = 0; i < len; i ++) {
        //一个int型占4个字节 ，一个字节8位
            int temp = 0;
            temp = temp | bytes[i];
            temp = temp << 8*i;
            integer = integer | temp;
        }
        return integer;
    }

    public static byte[] readBytes(InputStream in, long length) throws IOException {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int read = 0;
        while (read < length) {
            int cur = in.read(buffer, 0, (int)Math.min(1024, length - read));
            if (cur < 0) { break; }
            read += cur;
            bo.write(buffer, 0, cur);
        }
        return bo.toByteArray();
    }

    /**
     * 从输入流获取数据
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inputStream) throws Exception{
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1){
            System.out.print("test3");
            outputStream.write(buffer, 0, len);
        }
        outputStream.close();
        inputStream.close();
        return outputStream.toByteArray();
    }
}
