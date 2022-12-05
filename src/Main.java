public class Main {
    public static void main(String[] args) throws InterruptedException {
        Address[] addressArr = Address.getAllPossibleAddressesFromNetwork();

        long a = System.currentTimeMillis();

        Address[] pingable = Address.multiPing(addressArr);

        for (Address address : pingable)
            System.out.println(address);

        long b = System.currentTimeMillis();

        System.out.println("Total " + pingable.length + ((pingable.length != 1) ? " connections" : " connection"));
        System.out.println("Finished in " + Math.round((float)(b - a) / 1000) + "s");
    }
}
