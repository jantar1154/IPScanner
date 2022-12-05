import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class Address {
    private short[] numbers = new short[4];

    public static ArrayList<Address> pingable = new ArrayList<>();

    public static ArrayList<Integer> openPorts = new ArrayList<>();

    Address(String address) {
        String[] splitString = address.split("\\.", 4);
        for (int i = 0; i < 4; ) {
            this.numbers[i] = Short.parseShort(splitString[i]);
            i++;
        }
    }

    Address(short[] address) {
        this.numbers = address;
    }

    public boolean ping() {
        try {
            Inet4Address inet = (Inet4Address)Inet4Address.getByName(toString());
            return inet.isReachable(1000);
        } catch (IOException e) {
            return false;
        }
    }

    public static Address[] getAllPossibleAddressesFromNetwork() {
        Address netAddress = getNetAddress();
        Address lastAddress = getLastAddress();
        return allFromRange(netAddress, lastAddress);
    }

    private static Address getLastAddress() {
        Address localAddress = getLocalAddress();
        Address netMask = prefixLengthToMask(getMask());
        return new Address(new short[] { (short)(localAddress.numbers[0] | (netMask.numbers[0] ^ 0xFFFFFFFF) + 256), (short)(localAddress.numbers[1] | (netMask.numbers[1] ^ 0xFFFFFFFF) + 256), (short)(localAddress.numbers[2] | (netMask.numbers[2] ^ 0xFFFFFFFF) + 256), (short)(localAddress.numbers[3] | (netMask.numbers[3] ^ 0xFFFFFFFF) + 256) });
    }

    private static Address getNetAddress() {
        Address localAddress = getLocalAddress();
        Address netMask = prefixLengthToMask(getMask());
        return new Address(new short[] { (short)(localAddress.numbers[0] & netMask.numbers[0]), (short)(localAddress.numbers[1] & netMask.numbers[1]), (short)(localAddress.numbers[2] & netMask.numbers[2]), (short)(localAddress.numbers[3] & netMask.numbers[3]) });
    }

    private static Address prefixLengthToMask(short netMaskLength) {
        switch (netMaskLength) {
            case 8:

            case 23:

            case 22:

            case 25:

            case 30:

            case 16:

        }
        return

                new Address("255.255.255.0");
    }

    private static Address[] allFromRange(Address start, Address last) {
        List<Address> arr = new ArrayList<>();
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
                        arr.add(address);
                        d = (short)(d + 1);
                    }
                    c = (short)(c + 1);
                }
                b = (short)(b + 1);
            }
            a = (short)(a + 1);
        }
        Address[] list = new Address[arr.size()];
        for (int j = 0; j < arr.size(); j++)
            list[j] = arr.get(j);
        return list;
    }

    public static Address[] multiPing(Address[] addresses) throws InterruptedException {
        Thread[] threads = new Thread[addresses.length];
        for (int i = 0; i < addresses.length; i++)
            threads[i] = (new MultiPing(addresses[i])).start();
        for (Thread t : threads)
            t.join();
        Address[] rtn = new Address[pingable.size()];
        for (int j = 0; j < pingable.size(); ) {
            rtn[j] = pingable.get(j);
            j++;
        }
        return rtn;
    }

    public static Address getLocalAddress() {
        String inetAddress;
        try {
            inetAddress = Inet4Address.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return new Address("0");
        }
        return new Address(inetAddress);
    }

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
        return ((InterfaceAddress)networkInterface.getInterfaceAddresses().get(0)).getNetworkPrefixLength();
    }

    public String toString() {
        return "" + this.numbers[0] + "." + this.numbers[0] + "." + this.numbers[1] + "." + this.numbers[2];
    }
}
