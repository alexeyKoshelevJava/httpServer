import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private int port;
    private String directory;
    ExecutorService service = Executors.newFixedThreadPool(60);


    public Server(int port, String directory) {
        this.port = port;
        this.directory = directory;
    }

    void start() throws IOException {

        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();


            service.execute(new MyThread(socket, this.directory));




        }

    }



}

