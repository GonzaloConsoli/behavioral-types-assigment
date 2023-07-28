import java.net.*;

public class FileServerThread extends Thread {
  private Socket socket;

  public FileServerThread(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      FileServer server = new FileServer();
      if (server.start(socket)) {
        System.out.println("File server started!");
        if (server.hasRequest()) {
          System.out.println("Request found, waiting for filename to serve...");
          server.serveRequest();
          System.out.println("Served!");
        } else {
          System.out.println("No request found!");
        }
        server.close();
      } else {
        System.out.println("Could not start server!");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
