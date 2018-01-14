import core.Receiver;
import entity.FileInfo;
import interfaces.ITransferListener;

public class Server implements ITransferListener {

    // 发送端停止发送时，服务端会死循环读取数据

    public static void main(String[] args) {

        Server server = new Server();
        server.start();


    }


    public void start() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Receiver receiver = new Receiver(Server.this);
                // 接收
                receiver.receive();

            }
        }).start();

    }

    @Override
    public void onProgress(FileInfo file, int percent) {

    }

    @Override
    public void onFailed(String fileInfo) {

    }

    @Override
    public void onCompleted() {

    }
}
