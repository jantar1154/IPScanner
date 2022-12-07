import java.util.Arrays;
import java.util.Scanner;

// Main
public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.print("Ping ports? [Y/N]: ");
        boolean pingPorts = !new Scanner(System.in).nextLine().equalsIgnoreCase("N");
        System.out.print("\nSpeed (higher is slower): ");
        int divisions = new Scanner(System.in).nextInt();

        Address[] addressArr = Address.getAllPossibleAddressesFromNetwork();

        long a = System.currentTimeMillis();

        Address[] pingable = Address.multiPing(addressArr, divisions);

        for (Address address : pingable)
            System.out.println(address + (pingPorts ? ("\t" + Arrays.toString(PortTester.testForOpenPorts(address))) : ""));

        long b = System.currentTimeMillis();

        System.out.println("Total " + pingable.length + ((pingable.length != 1) ? " connections" : " connection"));
        System.out.println("Finished in " + Math.round((float)(b - a) / 1000) + "s");
    }
}
