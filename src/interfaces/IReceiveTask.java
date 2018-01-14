package interfaces;

public interface IReceiveTask {

    void init();
    void parseHeader();
    void parseBody();
    void release();



}
