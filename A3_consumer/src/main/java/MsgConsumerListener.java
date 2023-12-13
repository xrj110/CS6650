import com.rabbitmq.client.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@WebListener
public class MsgConsumerListener implements ServletContextListener {
    private final int maxThreads = 15;
    private final List<MsgConsumer> consumers = new ArrayList<>();


    public void contextInitialized(ServletContextEvent sce) {
        for (int i = 0; i < maxThreads; i++) {
            MsgConsumer consumer = new MsgConsumer();
            consumer.start();
            consumers.add(consumer);
        }
    }


    public void contextDestroyed(ServletContextEvent sce) {

    }
}

class MsgConsumer {
    private Connection connection;
    private Channel channel;
    private final static String QUEUE_NAME = "album";
    static DataSource dataSource = HikariCPDataSource.getDataSource();
    public void start() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost("172.31.28.132");
                    factory.setPort(5672);
                    factory.setUsername("guest");
                    factory.setPassword("guest");
                    factory.setVirtualHost("/");
                    connection = factory.newConnection();
                    channel = connection.createChannel();

                    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
                    System.out.println(" [*] Thread waiting for messages. To exit press CTRL+C");

                    Consumer consumer = new DefaultConsumer(channel) {
                        @Override
                        public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                            String message = new String(body, "UTF-8");
                            System.out.println(" [x] Thread Received '" + message + "'");
                            String[] ms = message.split("\\.");
                            boolean like=ms[0].equals("like");
                            String id=ms[1];
                            String updateQuery;
                            if (like){
                                updateQuery = "UPDATE likes SET l=l+1 WHERE album_id = ?";
                            }else {
                                updateQuery = "UPDATE likes SET un=un+1 WHERE album_id = ?";
                            }
                            java.sql.Connection DBconnection=null;
                            try {
                                DBconnection = dataSource.getConnection();
                                PreparedStatement updateStatement = DBconnection.prepareStatement(updateQuery);
                                updateStatement.setString(1,id);
                                updateStatement.executeUpdate();
                                System.out.println("add"+ms[0]+"to id:"+ms[1]);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }finally {
                                try {
                                    DBconnection.close();
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    };
                    channel.basicConsume(QUEUE_NAME, true, consumer);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }



}
