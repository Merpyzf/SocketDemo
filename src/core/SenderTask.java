package core;

import common.Common;
import entity.FileInfo;
import interfaces.ITransferListener;

import java.io.*;

public class SenderTask {

    // socket的输出流
    private OutputStream mOutputStream;
    private ITransferListener mTransferListener;


    public SenderTask(OutputStream outputStream, ITransferListener transferListener) {
        this.mOutputStream = outputStream;
        this.mTransferListener = transferListener;

    }

    /**
     * 发送文件
     *
     * @param file
     */
    public void send(FileInfo file) {

        long currentWriteLength = 0;
        File sendFile = new File(file.getFilePath());
        try {
            // 考虑文件在传输时故意删除导致文件不存无法传输的问题:
            // 在文件的头信息加入文件不存在的标记

             if (!sendFile.exists()) {
                // 当传输的文件不存在的时候，只发送头信息，接收端对头信息作出判断
                file.setFileStatus(Common.FileStatus.DELETE);
                sendHeader(file);
                return;
            }
            // 待传输的总字节数
            long total = Common.HEADER_LENGTH + sendFile.length();

            int headerLength = sendHeader(file);
            currentWriteLength+=headerLength;

            // 将待传输的文件内容部分写出
            byte[] buffer = new byte[Common.BUFFER_LENGTH];
            int readLength = -1;
            FileInputStream is = new FileInputStream(sendFile);
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(mOutputStream);
            DataOutputStream dos = new DataOutputStream(bos);

            while ((readLength = bis.read(buffer, 0, buffer.length)) != -1) {
                dos.write(buffer, 0, readLength);
                // 计算进度
                currentWriteLength += readLength;
                float percent = currentWriteLength / (total * 1.0f);
                file.setPercent((int) (percent * 100));
                if (mTransferListener != null) {
                    mTransferListener.onProgress(file, file.getPercent());
                }
            }

            dos.flush();
            // 关闭资源
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();

            callFailed(e.getMessage());

        } finally {
            if (file.getIsEnd() == 1) {
                // 释放资源
                try {
                    mTransferListener.onCompleted();
                    mOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    callFailed(e.getMessage());
                }
            }
        }

    }

    /**
     * 发送Header
     *
     * @param file
     * @return Header部分长度
     * @throws IOException
     */
    private int sendHeader(FileInfo file) throws IOException {



        byte[] headerBytes = file.getHeader().getBytes();
        mOutputStream.write(headerBytes, 0, headerBytes.length);
        return headerBytes.length;

    }


    /**
     * 发送异常时通知外界的回调方法
     *
     * @param error
     */
    public void callFailed(String error) {
        if (mTransferListener != null) {
            mTransferListener.onFailed(error);
        }
    }

}
