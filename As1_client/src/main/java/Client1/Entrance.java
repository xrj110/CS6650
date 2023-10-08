package Client1;

public class Entrance {
    public static void main(String[] args) {
        String goUrl="http://18.207.245.2:8081/CS6650A1/albums";
        String javaUrl="http://18.207.245.2:8080/CS6650A1/albums";

        try {
            System.out.println("---------------------Java-----------------------");

            Client1 clientToJava = new Client1();
            clientToJava.startTest(javaUrl);
            System.out.println("---------------------Go-----------------------");
            Client1 clientToGo = new Client1();
            clientToGo.startTest(goUrl);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
