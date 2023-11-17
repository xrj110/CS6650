package src;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@WebServlet("/review/*")
@MultipartConfig
public class ReviewServlet extends HttpServlet {
    private BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    @Override
    public void init() throws ServletException {

        for (int i = 0; i < 45; i++) {
            new Thread(new MessageProducer(messageQueue)).start();
        }
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String pathInfo = request.getPathInfo();


        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing likeornot and albumID");
            return;
        }


        String[] pathParts = pathInfo.split("/");


        if (pathParts.length != 3) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Incorrect path format");
            return;
        }


        String likeornot = pathParts[1];
        String albumID = pathParts[2];



        try {
            messageQueue.put(likeornot + "." + albumID);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Interrupted while adding the message to the queue", e);
        }






        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");


        response.getWriter().write("Received likeornot: " + likeornot + " and albumID: " + albumID);
    }
    private static class MessageProducer implements Runnable {
        private BlockingQueue<String> queue;

        public MessageProducer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                initialize();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            while (true) {
                try {
                    String message = queue.take();

                    send(message);
                } catch (InterruptedException e) {

                    break;
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }
        }
        ConnectionFactory factory;
        Channel channel;
        public void initialize() throws Exception{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("100.26.206.226");
            factory.setPort(5672);
            factory.setUsername("guest");
            factory.setPassword("guest");
            factory.setVirtualHost("/");
            Connection connection = factory.newConnection();
            channel=connection.createChannel();
        }
        private final static String QUEUE_NAME = "album";


        public void send(String message) throws Exception {


            try  {


                channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" [x] Sent '" + message + "'");
            }catch (Exception e){
                System.out.println(e);
            }
        }
    }
}
