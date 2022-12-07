
/**
 * This class calls the ping() function on many threads
 * Use MultiPing.start()
 */
public class MultiPing implements Runnable {
    private Thread thr;

    private final Address address;

    MultiPing(Address address) {
        this.address = address;
    }

    public void run() {
        if (this.address.ping())
            Address.pingable.add(this.address);
    }

    public Thread start() {
        if (this.thr == null) {
            this.thr = new Thread(this);
            this.thr.start();
            return this.thr;
        }
        return null;
    }
}
