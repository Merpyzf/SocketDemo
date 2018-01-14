import java.io.*;

public class Test {

    public static void main(String[] args) throws IOException {


        int i = 0;
        System.out.println(i);

    }


    /**
     * 读取内容部分
     *
     * @param is
     */
    private static void readBody(FileInputStream is) throws IOException, InterruptedException {

        // file total length 30 byte

        byte[] buffer = new byte[10];
        byte[] bytes = new byte[2];
        int readLength = 0;

        int available = is.available();
        // is available length
        System.out.println("is.available:" + available);

        BufferedInputStream bis = new BufferedInputStream(is);
        readLength = bis.read(buffer);

        System.out.println("read length:" + readLength + "read length:" + new String(buffer));

        // bis available length
        System.out.println("bis.available:" + bis.available());
        // is available length
        System.out.println("is.available:" + is.available());

        readLength = bis.read(buffer);
        System.out.println("read length:" + readLength + "content:" + new String(buffer));

        // is available length
        System.out.println("is.available:" + is.available());
        readLength = is.read(bytes);
        System.out.println("read length:" + readLength + "content:" + new String(bytes));
    }


    /**
     * 读取头部
     */
    private static void readHeader(InputStream is) throws IOException {

        byte read = (byte) is.read();
        System.out.println("读取的内容:" + read);


    }
}
