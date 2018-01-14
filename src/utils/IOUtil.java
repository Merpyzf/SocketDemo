package utils;

import common.Common;
import entity.FileInfo;

import java.io.*;

public class IOUtil {

    /**
     * 将输入流写出到输出流
     * @param is 输入流
     * @param os 输出流
     */
    public static void write(FileInfo fileInfo, InputStream is, OutputStream os){


        byte[] buffer = new byte[Common.BUFFER_LENGTH];
        int readLength = -1;

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);
        // 记录总写出的长度，用来计算文件的传输进度
        long totalLength = 0;
        try {
            while ((readLength = bis.read(buffer,0,buffer.length))!=-1){
                dos.write(buffer,0,readLength);
                // 计算进度
                totalLength+=readLength;
                float percent = totalLength/(fileInfo.getFileLength()*1.0f);
                fileInfo.setPercent((int) (percent*100));
                System.out.println(fileInfo.getFileName()+": 传输进度 > > >"+fileInfo.getPercent());
            }

            dos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }



    public static void write( InputStream is, OutputStream os){


        byte[] buffer = new byte[1024];

        int length = -1;

        BufferedInputStream bis = new BufferedInputStream(is);
        BufferedOutputStream bos = new BufferedOutputStream(os);
        DataOutputStream dos = new DataOutputStream(bos);

        try {
            while ((length = bis.read(buffer,0,buffer.length))!=-1){

//                Thread.sleep(2000);

                dos.write(buffer,0,length);



            }

            bos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            try {
                bis.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
