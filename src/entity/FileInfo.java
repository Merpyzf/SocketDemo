package entity;

import common.Common;

public class FileInfo {

    // 文件名
    private String fileName;
    // 文件路径
    private String filePath;
    // 文件大小
    private long fileLength;
    // 传输进度
    private int percent;
    // 是否传输结束
    private boolean isCompleted = false;
    // 标记是否是最后一个文件
    private int isEnd = 0;
    // 文件状态
    private int fileStatus = Common.FileStatus.NORMAL;

    public FileInfo() {

    }

    public int getFileStatus() {
        return fileStatus;
    }

    public void setFileStatus(int fileStatus) {
        this.fileStatus = fileStatus;
    }

    public FileInfo(String fileName, long fileLength) {
        this.fileName = fileName;
        this.fileLength = fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getFileLength() {
        return fileLength;
    }

    public void setFileLength(long fileLength) {
        this.fileLength = fileLength;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }


    public int getIsEnd() {
        return isEnd;
    }

    public void setIsEnd(int isEnd) {
        this.isEnd = isEnd;
    }

    /**
     * 要发送给服务端的文件信息
     * 1024个字节
     *
     * @return
     */
    public String getHeader() {

        StringBuffer sb = new StringBuffer();
        sb.append(fileName);
        sb.append(":");
        sb.append(fileLength);
        sb.append(":");
        sb.append(isEnd);
        sb.append(":");
        sb.append(fileStatus);
        sb.append(Common.S_END);

        // 当前文件头部的长度
        int currentLength = sb.toString().getBytes().length;

        if (currentLength < Common.HEADER_LENGTH) {
            // 少于的部分使用空格填充
            for (int i = 0; i < Common.HEADER_LENGTH - currentLength; i++) {

                sb.append("k");

            }

        }

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileInfo = (FileInfo) o;

        if (fileLength != fileInfo.fileLength) return false;
        if (percent != fileInfo.percent) return false;
        if (isCompleted != fileInfo.isCompleted) return false;
        if (isEnd != fileInfo.isEnd) return false;
        if (fileName != null ? !fileName.equals(fileInfo.fileName) : fileInfo.fileName != null) return false;
        return filePath != null ? filePath.equals(fileInfo.filePath) : fileInfo.filePath == null;
    }
}
