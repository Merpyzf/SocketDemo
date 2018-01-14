package interfaces;

import entity.FileInfo;

public interface ITransferListener {

    void onProgress(FileInfo file, int percent);
    void onFailed(String fileInfo);
    void onCompleted();

}
