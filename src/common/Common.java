package common;

public class Common {

    public static final int PORT = 8088;
    // 文件信息头部使用1024个字节进行存储
    public static  int HEADER_LENGTH = 1024;

    public static final String HOST_ADDRESS = "127.0.0.1";

//    public static final String HOST_ADDRESS = "172.28.67.124";

    public static final int BUFFER_LENGTH = 8192;

    public static final String S_END = "\0";


    public class FileStatus{

        // 正常状态
        public static final int NORMAL = 1;

        // 被删除
        public static final int DELETE = 2;

    }

}
