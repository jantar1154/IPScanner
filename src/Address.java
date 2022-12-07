import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class Address {
    private short[] numbers = new short[4];

    public static ArrayList<Address> pingable = new ArrayList<>();

    public static ArrayList<Integer> openPorts = new ArrayList<>();

    Address(String address) {
        String[] arr = address.split("\\.", 4);
        for (int i = 0; i < 4; ) {
            this.numbers[i] = Short.parseShort(arr[i]);
            i++;
        }
    }

    Address(short[] address) {
        this.numbers = address;
    }

    // Pings this address
    public boolean ping() {
        try {
            Inet4Address inet = (Inet4Address) Inet4Address.getByName(this.toString());
            return inet.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }

    // Returns an array of every possible IP address of network device running this program is in
    public static Address[] getAllPossibleAddressesFromNetwork() {
        Address netAddress = getNetAddress();
        Address lastAddress = getLastAddress();
        return allFromRange(netAddress, lastAddress);
    }

    // Gets last IP on your network before broadcast
    public static Address getLastAddress() {
        Address localAddress = getLocalAddress();
        Address netMask = prefixLengthToMask(getMask());

        return new Address(new short[] {
                (short)(localAddress.numbers[0] | ~(netMask.numbers[0]) + 256),
                (short)(localAddress.numbers[1] | ~(netMask.numbers[1]) + 256),
                (short)(localAddress.numbers[2] | ~(netMask.numbers[2]) + 256),
                (short)(localAddress.numbers[3] | ~(netMask.numbers[3]) + 256)
        });
    }

    // Returns IP address of your network
    public static Address getNetAddress() {

        Address localAddress = getLocalAddress();
        Address netMask = prefixLengthToMask(getMask());

        return new Address(new short[] {
                (short) (localAddress.numbers[0] & (netMask.numbers[0])),
                (short) (localAddress.numbers[1] & (netMask.numbers[1])),
                (short) (localAddress.numbers[2] & (netMask.numbers[2])),
                (short) (localAddress.numbers[3] & (netMask.numbers[3]))
        });
    }

    // "Converts" prefix length (/24) to mask (255.255.255.0)
    private static Address prefixLengthToMask(short netMaskLength) {
        return switch (netMaskLength) {
            case 8 -> new Address("255.0.0.0");
            case 23 -> new Address("255.255.254.0");
            case 22 -> new Address("255.255.252.0");
            case 25 -> new Address("255.255.255.128");
            case 30 -> new Address("255.255.255.252");
            case 16 -> new Address("255.255.0.0");
            default -> new Address("255.255.255.0");
        };
    }

    // Creates an array containing every possible IP address between start and last argument
    private static Address[] allFromRange(Address start, Address last) {
        List<Address> addressList = new ArrayList<>();

        int aLen = Math.abs(start.numbers[0] - last.numbers[0]);
        int bLen = Math.abs(start.numbers[1] - last.numbers[1]);
        int cLen = Math.abs(start.numbers[2] - last.numbers[2]);
        int dLen = Math.abs(start.numbers[3] - last.numbers[3]);

        short a = start.numbers[0];
        short b = start.numbers[1];
        short c = start.numbers[2];
        short d = start.numbers[3];

        for (int i = 0; i < aLen + 1; i++) {
            for (int k = 0; k < bLen + 1; k++) {
                for (int m = 0; m < cLen + 1; m++) {
                    for (int l = 0; l < dLen + 1; l++) {
                        Address address = new Address(new short[] { a, b, c, d });
                        addressList.add(address);
                        d ++;
                    }
                    c ++;
                }
                b ++;
            }
            a ++;
        }

        Address[] list = new Address[addressList.size()];

        for (int i = 0; i < addressList.size(); i++)
            list[i] = addressList.get(i);

        return list;
    }

    // Pings every IP address from array addresses in parallel
    public static Address[] multiPing(Address[] addresses, int divider) throws InterruptedException {
        Thread[] threads = new Thread[addresses.length];

        for (int i = 0; i < divider; i++) {
            for (int j = 0; j < addresses.length/divider; j++) {
                int index = j + (i* addresses.length/divider);
                threads[index] = new MultiPing(addresses[index]).start();
                System.out.println(threads[index] + "\tindex: " + index);
            }
            Thread.sleep(1000);
        }

        //for (Thread t : threads) if (t != null) t.join();

        Address[] rtn = new Address[pingable.size()];

        for (int j = 0; j < pingable.size(); ) {
            rtn[j] = pingable.get(j);
            j++;
        }

        return rtn;
    }

    // Returns IP address of device running this program
    public static Address getLocalAddress() {
        String inetAddress;
        try {
            inetAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return new Address("");
        }
        return new Address(inetAddress);
    }

    // Returns netmask of network in which is device running this program
    public static short getMask() {
        InetAddress localHost;
        NetworkInterface networkInterface;
        try {
            localHost = Inet4Address.getLocalHost();
        } catch (UnknownHostException e) {
            return 0;
        }
        try {
            networkInterface = NetworkInterface.getByInetAddress(localHost);
        } catch (SocketException e) {
            return 0;
        }
        return (networkInterface.getInterfaceAddresses().get(0)).getNetworkPrefixLength();
    }

    // Returns instance of this class in String
    @Override
    public String toString() {
        return this.numbers[0] + "."
                + this.numbers[1] + "."
                + this.numbers[2] + "."
                + this.numbers[3];
    }
}
