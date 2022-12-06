public class MultiPing implements Runnable {
    private Thread t;

    private final Address address;

    MultiPing(Address address) {
        this.address = address;
    }

    public void run() {
        if (this.address.ping())
            Address.pingable.add(this.address);
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
