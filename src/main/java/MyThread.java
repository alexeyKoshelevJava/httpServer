import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MyThread extends Thread {
    private final Socket socket;
    private final String directory;
    private final List<String> validPath = List.of("/text.txt", "/spring.svg", "/streptatest4.png", "/index.html","/VID-1.mp4");
    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";

    public MyThread(Socket socket, String directory) {
        this.socket = socket;
        this.directory = directory;

    }

    @Override
    public synchronized void run() {

        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                OutputStream out = socket.getOutputStream()) {


            String line = in.readLine();
                        System.out.println(line);

            String[] request;
            if (line != null) {
                request = line.split(" ");


                String url = request[1];
                Path filePath = Path.of(directory + url);

                String mineType = Files.probeContentType(filePath);
                System.out.println(Thread.currentThread() + " выполняю запрос " + mineType);


                if (validPath.contains(url)) {
                    byte[] fileBytes = Files.readAllBytes(filePath);
                    sendHeader(out, 200, "OK", mineType, fileBytes);
                    out.write(fileBytes);
                    out.flush();

                } else {

                    sendHeader(out, 400, NOT_FOUND_MESSAGE, mineType, NOT_FOUND_MESSAGE.getBytes());
                    out.write(NOT_FOUND_MESSAGE.getBytes());

                }
            }

        } catch(IOException e){
                e.printStackTrace();
            }


    }

    private void sendHeader(OutputStream out, int statusCode, String statusText, String mineType, byte[] fileBytes) throws IOException {
        out.write(("HTTP/1.1" + statusCode + statusText + "\r\n" +
                "Content-Type: " + mineType + "\r\n" +
                "Content-Length: " + fileBytes.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n"
        ).getBytes());
    }
}