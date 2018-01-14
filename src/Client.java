import core.Sender;
import entity.FileInfo;
import interfaces.ITransferListener;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Client implements ITransferListener {
    private List<FileInfo> mFileInfoList = null;

    public static void main(String[] args) {


        Client mainSend = new Client();
        mainSend.start();

    }

    private void start() {

        initData();
        Sender sender = new Sender(this);
        sender.send(mFileInfoList);

    }

    /**
     * 模拟一组待发送的文件数据
     */
    private void initData() {

        File[] sendFiles = new File("sned_file").listFiles(new FileFilter() {
            // 过滤隐藏文件
            @Override
            public boolean accept(File pathname) {
                return !pathname.getName().startsWith(".");
            }
        });
        mFileInfoList = new ArrayList<>();
        for (int i = 0; i < sendFiles.length; i++) {
            File file = new File(sendFiles[i].getPath());
            FileInfo fileInfo = new FileInfo();
            // 文件大小
            fileInfo.setFileLength(file.length());
            // 文件名
            fileInfo.setFileName(file.getName());
            // 文件路径
            fileInfo.setFilePath(sendFiles[i].getPath());
            if (i == sendFiles.length - 1) {
                // 将本组待传输文件的最后一个文件标记为1
                fileInfo.setIsEnd(1);
            }
            mFileInfoList.add(fileInfo);
        }


    }


    @Override
    public void onProgress(FileInfo file, int percent) {
        System.out.println(file.getFileName() + "进度: " + percent);
    }

    @Override
    public void onFailed(String error) {
        System.out.println(error);
    }

    @Override
    public void onCompleted() {
        System.out.println("发送完毕");
    }
}
