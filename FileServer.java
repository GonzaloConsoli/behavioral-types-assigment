import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.*;
import jatyc.lib.Typestate;

@Typestate("FileServer")
public class FileServer {
  private Socket socket;
  protected OutputStream out;
  protected BufferedReader in;
  protected String lastFilename;
  protected byte[] fileContents;
  protected int idxFileContents = 0;

  public boolean start(Socket s) {
    try {
      socket = s;
      out = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public boolean hasRequest() throws Exception {
    String command = in.readLine();
    if (command != null && command.equals("REQUEST")) {
      return true;
    }
    return false;
  }

  public void readFileName() throws Exception {
    fileContents = new byte[] {};
    String filename = in.readLine();
    if (filename != null) {
      File f = new File(filename);
      if (f.exists()) {
        Path p = f.toPath();
        if (p != null) {
          byte[] temp = Files.readAllBytes(p);
          if (temp != null){
            fileContents = temp;
          }
        }
      }
    }

  }

  public boolean theresNextByte() {
    return idxFileContents < fileContents.length;
  }

  public void sendByte() throws Exception {
    out.write(fileContents[idxFileContents++]);
  }

  public void sendNullByte() throws Exception {
    out.write(0);
  }

  public void close() throws Exception {
    in.close();
    if (!socket.isClosed()) {
      socket.close();
    }
  }

  public static void main(String[] args) throws Exception {
    ServerSocket serverSocket = new ServerSocket(1234);
    while (true) {
      new FileServerThread(serverSocket.accept()).start();
    }
  }
}
