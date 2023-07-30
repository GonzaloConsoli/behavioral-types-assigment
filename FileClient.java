import java.net.*;
import java.util.ArrayList;
import java.io.*;
import jatyc.lib.Typestate;

@Typestate("FileClient")
public class FileClient {
  private Socket socket;
  protected OutputStream out;
  protected BufferedReader in;
  protected int lastByte;
  protected boolean isEof=false;

  public boolean start() {
    try {
      socket = new Socket("localhost", 1234);
      out = socket.getOutputStream();
      in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  public void request(String filename) throws Exception {
    out.write("REQUEST\n".getBytes());
    out.write((filename + "\n").getBytes());
  }

  public byte acceptResponse() throws Exception {
    int readByte = in.read();
    if (readByte == 0){
      isEof = true;
    }
    return (byte) readByte;
  }

  public boolean isEof(){
    return isEof;
  }

  public void close() throws Exception {
    in.close();
    if (!socket.isClosed()) {
      socket.close();
    }
  }

  public static void main(String[] args) throws Exception {
    FileClient client = new FileClient();
    if (client.start()) {
      System.out.println("File client started!");
      System.out.println("Asking for file example...");
      client.request("example");
      int counter = 0;
      while( ! client.isEof() ){
        client.acceptResponse();
        counter++;
      }
      System.out.println("Server gave us back " + counter + " bytes!");
      client.close();
    } else {
      System.out.println("Could not start client!");
    }
  }
}
