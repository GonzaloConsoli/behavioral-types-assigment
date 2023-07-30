import jatyc.lib.Typestate;

@Typestate("FileClient2")
public class FileClient2 extends FileClient {

    public String acceptResponseByLine() throws Exception {
        String line = "";
        byte lastRead = (byte) 255;
        while (!isEof && lastRead != 10) {
            lastRead = (byte) in.read();
            if (lastRead != 0 && lastRead != 10) {
                line = line + new String(new char[] { (char) lastRead });
            } else if (lastRead == 0) {
                isEof = true;
            }
        }
        return line;
    }

    public static void main(String[] args) throws Exception {
        FileClient2 client = new FileClient2();
        if (client.start()) {
            System.out.println("File client started!");
            System.out.println("Asking for file example...");
            client.request("example");
            int counter = 0;
            while (!client.isEof()) {
                client.acceptResponseByLine();
                counter++;
            }
            System.out.println("Server gave us back " + counter + " lines!");
            client.close();
        } else {
            System.out.println("Could not start client!");
        }
    }
}
