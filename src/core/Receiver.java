package core;

import common.Common;
import interfaces.ITransferListener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 调度
 */
public class Receiver {

    public ExecutorService mThreadPool;
    private  ServerSocket mServerSocket;
    private ITransferListener mTransferListener;

    public Receiver(ITransferListener transferListener) {
        this.mTransferListener = transferListener;
        mThreadPool = Executors.newCachedThreadPool();
        try {
            mServerSocket = new ServerSocket();
            mServerSocket.bind(new InetSocketAddress(Common.PORT));
        } catch (IOException e) {
            e.printStackTrace();
            callFailed(e.getMessage());
        }

    }

    /**
     * 等待设备连接接收文件
     */
    public void receive(){

        try {

            while (true){

                System.out.println("waiting....");
                Socket client = mServerSocket.accept();
                System.out.println("设备地址:"+client.getInetAddress().getHostAddress());
                mThreadPool.execute(new ReceiveTask(client, mTransferListener));

            }

        } catch (IOException e) {
            e.printStackTrace();
            callFailed(e.getMessage());
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
