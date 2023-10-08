package Client2;

public class Entrance {
    public static void main(String[] args) {
        String goUrl="http://44.201.122.207:8081/CS6650A1/albums";
        String javaUrl="http://44.201.122.207:8080/CS6650A1/albums";

        try {
            System.out.println("---------------------Go--------------------------------------------------------------------");
            Client2 clientToGo = new Client2();
            clientToGo.startTest(goUrl,"GoRecord.csv");
            System.out.println("---------------------Java------------------------------------------------------------------");
            Client2 clientToJava = new Client2();
            clientToJava.startTest(javaUrl,"JavaRecord.csv");
            Plot.generate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
