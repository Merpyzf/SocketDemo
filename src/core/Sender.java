package core;

import common.Common;
import entity.FileInfo;
import interfaces.ITransferListener;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;

/**
 * 负责文件的发送
 */
public class Sender {

    private SenderTask mSenderTask;

    private Socket mSocket = null;
    private ITransferListener mTransferListener;

    public Sender() {
        init();
    }

    public Sender(ITransferListener mTransferListener) {
        this.mTransferListener = mTransferListener;
        init();
    }

    /**
     * 初始化Socket
     */
    private void init() {
        try {
            mSocket = new Socket(Common.HOST_ADDRESS, Common.PORT);
            mSenderTask = new SenderTask(mSocket.getOutputStream(), mTransferListener);
        } catch (SocketException e) {
            e.printStackTrace();
            callFailed(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            callFailed(e.getMessage());
        }
    }

    /**
     * 发送文件
     *
     * @param fileList 存放一组文件的list集合
     */
    public void send(List<FileInfo> fileList) {

        boolean isConn = mSocket.isConnected();
        OutputStream outputStream = null;
        // 当建立连接才发送文件
        if (isConn) {
            // 遍历待传输的文件集合
            for (int i = 0; i < fileList.size(); i++) {
                mSenderTask.send(fileList.get(i));
            }
        }else {
            callFailed("还未与Server建立连接");
        }

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
