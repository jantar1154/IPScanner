import java.io.IOException;
import java.net.Socket;

public class PortTester implements Runnable {
    private Thread t;

    private final Address address;

    private final int port;

    private PortTester(Address address, int port) {
        this.address = address;
        this.port = port;
    }

    // Finds every open port on IP address by opening it
    public static int[] testForOpenPorts(Address address) throws InterruptedException {
        Thread[] threads = new Thread[1024];
        for (int i = 0; i < 1024; i++)
            threads[i] = (new PortTester(address, i)).start();
        for (Thread t : threads)
            t.join();
        int[] rtn = new int[Address.openPorts.size()];
        for (int j = 0; j < Address.openPorts.size(); ) {
            rtn[j] = Address.openPorts.get(j);
            j++;
        }
        Address.openPorts.clear();
        return rtn;
    }

    public void run() {
        try {
            Socket socket = new Socket(this.address.toString(), this.port);
            Address.openPorts.add(this.port);
            socket.close();
        } catch (IOException ignored) {}
    }

    public Thread start() {
        if (this.t == null) {
            this.t = new Thread(this);
            this.t.start();
            return this.t;
        }
        return null;
    }
}
