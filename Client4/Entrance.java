package Client4;

public class Entrance {
    public static void main(String[] args) {

        String javaUrl="http://ass4-1046218035.us-west-2.elb.amazonaws.com:8080/CS6650A4/";

        try {
            System.out.println("---------------------Java------------------------------------------------------------------");
            Client4 clientToJava = new Client4();
            clientToJava.startTest(javaUrl);


           // Plot.generate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
