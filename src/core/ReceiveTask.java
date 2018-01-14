package core;

import common.Common;
import entity.FileInfo;
import interfaces.IReceiveTask;
import interfaces.ITransferListener;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件接收任务
 */
public class ReceiveTask implements Runnable, IReceiveTask {

    private Socket mSocket;
    private List<FileInfo> mReceiveFileList;
    private InputStream mInputstream;
    private ITransferListener mTransferListener;
    private FileInfo mFileInfo;
    private int total = 0;
    private int currentReadLength = 0;



    public ReceiveTask(Socket socket, ITransferListener transferListener) {
        this.mTransferListener = transferListener;
        this.mSocket = socket;
        init();

    }

    @Override
    public void run() {

        long start = System.currentTimeMillis();
        try {
            while (true) {

                parseHeader();
                parseBody();
                if (mFileInfo.getIsEnd() == 1) {
                    System.out.println(">> 已经是最后一个文件了,跳出循环");
                    break;
                }
            }
        } finally {
            try {
                mInputstream.close();
                mTransferListener.onCompleted();
            } catch (IOException e) {
                e.printStackTrace();
                callFailed(e.getMessage());

            }

        }

        long end = System.currentTimeMillis();
        System.out.println("本次传输花费的时间:" + (end - start) / 1000f);

    }

    /**
     * 接收文件，将从Socket中获取的输入流写入到文件
     *
     * @param fileLength  本次待接收的文件的长度
     * @param inputStream socket中的输入流
     * @param receiveFile 待写入的文件
     * @throws IOException
     */
    private void receiveFile(long fileLength, InputStream inputStream, File receiveFile) throws IOException {
        int totalSize = 0;

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(receiveFile));
        byte[] buffer = new byte[Common.BUFFER_LENGTH];
        // 读取文件
        while (fileLength > 0) {
            int readLength = 0;
            if (fileLength > Common.BUFFER_LENGTH) {
                readLength = inputStream.read(buffer, 0, Common.BUFFER_LENGTH);
            } else {
                readLength = inputStream.read(buffer, 0, (int) fileLength);
            }
            bos.write(buffer, 0, readLength);

            totalSize += readLength;
            fileLength -= readLength;
        }
        System.out.println("写入到文件的字节数:" + totalSize);
        bos.flush();
        bos.close();
        bos.close();
    }

    @Override
    public void init() {

        try {
            this.mInputstream = mSocket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();

        }
        mReceiveFileList = new ArrayList<>();


    }

    /**
     * 解析header部分
     */
    @Override
    public void parseHeader() {

        byte[] buffer = new byte[Common.HEADER_LENGTH];

        try {

            int read = mInputstream.read(buffer, 0, buffer.length);
            System.out.println("头信息的长度:" + read);
            //将读取的头信息转换成字节数组
            String str = new String(buffer, "utf-8");
            // 解析到的头部字符串
            String strHeader = str.substring(0, str.indexOf(Common.S_END));
            System.out.println("header:" + strHeader);
            String[] propertys = strHeader.split(":");
            mFileInfo = new FileInfo();
            mFileInfo.setFileName(propertys[0]);
            mFileInfo.setFileLength(Long.valueOf(propertys[1]));
            mFileInfo.setIsEnd(Integer.valueOf(propertys[2]));
            mFileInfo.setFileStatus(Integer.valueOf(propertys[3]));

            printFileInfo(mFileInfo);


        } catch (IOException e) {
            e.printStackTrace();
            try {
                mInputstream.close();
                callFailed(e.getMessage());
            } catch (IOException e1) {
                e1.printStackTrace();
                callFailed(e.getMessage());
            }

        }
    }



    /**
     * 解析文件内容
     */
    @Override
    public void parseBody() {

        System.out.println(mFileInfo.getFileName());
        if (mFileInfo.getFileStatus() == Common.FileStatus.DELETE) {
            System.out.println(mFileInfo + " 在传输的过程中被删除了");
            return;
        }
        File receive_file = new File("receive_file", mFileInfo.getFileName());
        //接收文件
        try {
            receiveFile(mFileInfo.getFileLength(), mInputstream, receive_file);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 释放资源
     */
    @Override
    public void release() {
        // 释资源放
        if (mInputstream != null) {
            try {
                mInputstream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (mSocket != null && mSocket.isConnected()) {
            try {
                mSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 传输失败的回调
     */
    public void callFailed(String error) {
        if (mTransferListener != null) {
            mTransferListener.onFailed(error);
        }
    }


    private void printFileInfo(FileInfo mFileInfo) {

        System.out.println("********************");
        System.out.println("-> 待接收文件名:" + mFileInfo.getFileName() + "\n" + "-> 文件大小:" + mFileInfo.getFileLength() + "\n-> 最后一个文件:" + mFileInfo.getIsEnd());
        System.out.println("********************");

    }


}
