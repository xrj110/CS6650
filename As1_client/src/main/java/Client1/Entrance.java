package Client1;

public class Entrance {
    public static void main(String[] args) {
        String url="http://3.82.94.103:8081/CS6650A1/albums";
        System.out.println("---------------------Go-----------------------");
        Client1 clientToGo = new Client1();
        try {
            clientToGo.startTest(url);
            System.out.println("---------------------Java-----------------------");
            Client1 clientToJava = new Client1();
            clientToJava.startTest(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
