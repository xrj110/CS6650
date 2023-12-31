package Client1;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;


public class Client1 {
    private static AtomicInteger successfulPost = new AtomicInteger(0);
    private static AtomicInteger successfulGet = new AtomicInteger(0);
    public static void clear(){
        successfulPost.set(0);
        successfulGet.set(0);
    }





    public static void doPost(String path, Formdata data) throws Exception{
        URL url = new URL(path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


          connection.setRequestMethod("POST");
            connection.setDoOutput(true);





        String boundary = "*****"; //
        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        OutputStream outputStream = connection.getOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"), true);

        // Client1.Formdata
        writer.println("--" + boundary);
        writer.println("Content-Disposition: form-data; name=\"year\"");
        writer.println();
        writer.println(data.year);

        writer.println("--" + boundary);
        writer.println("Content-Disposition: form-data; name=\"title\"");
        writer.println();
        writer.println(data.title);

        writer.println("--" + boundary);
        writer.println("Content-Disposition: form-data; name=\"artist\"");
        writer.println();
        writer.println(data.artist);

        // file
        writer.println("--" + boundary);
        writer.println("Content-Disposition: form-data; name=\"image\"; filename=\"" + data.image.getName() + "\"");
        writer.println("Content-Type: application/octet-stream");
        writer.println();
        FileInputStream fileInputStream = new FileInputStream(data.image);
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();

        // Client1.Formdata ending
        writer.println();
        writer.println("--" + boundary + "--");
        writer.close();
        long s=System.currentTimeMillis();
        if (connection.getResponseCode()>400&&connection.getResponseCode()<600){
            throw new IOException();
        }
        //long e=System.currentTimeMillis()-s;
        //System.out.println("post:"+e);

        // response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        // handle response
        Gson gson = new Gson();
        Pres p = gson.fromJson(response.toString(), Pres.class);
      //  System.out.println(p.toString());


        connection.disconnect();
    }
    public static void doGet(String path) throws Exception {
        String id="1";
        URL url = new URL(path+"/"+id);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
       // long s=System.currentTimeMillis();
        if (connection.getResponseCode()>400&&connection.getResponseCode()<600){
            throw new IOException();
        }
        //long e=System.currentTimeMillis()-s;
       // System.out.println("get:"+e);
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder response = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();

        Gson gson=new Gson();
        Gres res=gson.fromJson(response.toString(), Gres.class);

        //System.out.println(res.toString());


        connection.disconnect();
    }
    public  void call(int threadGroupSize,int numThreadGroups,double delay,String path) throws InterruptedException {


        ExecutorService executor = Executors.newFixedThreadPool(threadGroupSize * numThreadGroups+10);
        long startTime = System.currentTimeMillis();
        //On startup, the program creates 10 threads, and each thread calls the POST API followed by the GET API 100 times in a simple loop. IPAddr is the base address of your server URI. This is basically a hardcoded initialization phase.
        for(int i=0;i<10;i++){
           executor.submit(new LoadTestThread(path,100));
        }

        for (int i = 0; i < numThreadGroups; i++) {
            for (int j = 0; j < threadGroupSize; j++) {
                executor.submit(new LoadTestThread(path,1000));

            }
            Thread.sleep((int)delay*1000);
        }
        executor.shutdown();

        try {
            // wait all threads
            if (!executor.awaitTermination(800, TimeUnit.SECONDS)) {
                // out of time
                System.out.println("Not all threads completed within the timeout.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis(); // record end time
        long totalTime = (endTime - startTime)/1000; // calculate total time

        System.out.println("The wall time: " + totalTime + " seconds");
        long total_request=successfulGet.get()+successfulPost.get();
        System.out.println("The Number of Successful Get Request:"+successfulGet.get());
        System.out.println("The Number of Successful Post Request:"+successfulPost.get());
        System.out.println("The Throughput: "+total_request/totalTime);
    }



    public void startTest(String url) throws Exception{


        System.out.println(" group1:");
        call(10,10,2,url);//127.0.0.1
        clear();
        System.out.println("-----------------------------------------");
        System.out.println("group2:");
        call(10,20,2,url);
        Client1.clear();
        System.out.println("-----------------------------------------");
        System.out.println("group3:");
        call(10,30,2,url);
        Client1.clear();

    }
    class LoadTestThread implements Runnable {
        private final String IPAddr;

        Formdata data=new Formdata();
        int runtime=1000;

        public String getIPAddr() {
            return IPAddr;
        }






        public LoadTestThread(String IPAddr, int runtime) {
            this.IPAddr = IPAddr;
            this.runtime=runtime;
        }

        @Override
        public void run() {

            // Make a POST API call here using the IPAddr

            for (int i = 0; i < runtime; i++) {
                int retry=5;

                // Make a GET API call here using the IPAddr
                while (retry-->0){
                    try {
                        Client1.doPost(IPAddr,new Formdata());
                        //PostSucessTime+=1;
                        successfulPost.incrementAndGet();
                        break;
                    } catch (Exception e) {
                        if(retry>0) continue;
                        else {
                            System.out.println(e);

                        }

                    }
                }
                retry=5;
                while (retry-->0){
                    try {
                        Client1.doGet(IPAddr);
                        successfulGet.incrementAndGet();
                        break;
                    } catch (Exception e) {
                        if(retry>0) continue;
                        else {
                            System.out.println(e);

                        }

                    }
                }


            }
        }
    }
}

