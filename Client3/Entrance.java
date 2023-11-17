package Client3;

public class Entrance {
    public static void main(String[] args) {

        String javaUrl="http://54.84.35.171:8080/CS6650A2/";

        try {
            System.out.println("---------------------Java------------------------------------------------------------------");
            Client3 clientToJava = new Client3();
            clientToJava.startTest(javaUrl);


           // Plot.generate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
