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

  public Byte[] acceptResponse() throws Exception {
    ArrayList<Byte> response = new ArrayList<Byte>();

    // Read the response
    int readByte = 0;
    do {
      readByte = in.read();
      response.add((byte)readByte);
    } while (readByte != 0);

    // Cast it to a primitive array, since we can't handle generics
    Byte[] res = new Byte[response.size()];
    for (int i = 0; i < res.length; i++) {
      res[i] = response.get(i);
    }
    return res;
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
      int bytesRead = client.acceptResponse().length;
      System.out.println("Server gave us back " + bytesRead + " bytes!");
      client.close();
    } else {
      System.out.println("Could not start client!");
    }
  }
}
