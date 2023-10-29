package src;

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

@WebServlet("/albums/*")
@MultipartConfig
public class AlbumServlet extends HttpServlet {
    static DataSource dataSource = HikariCPDataSource.getDataSource();
    static final int serviceId=1;


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String artist = request.getParameter("artist");
        String title=request.getParameter("title");
        String year=request.getParameter("year");
        Part imagePart = request.getPart("image");
        if(artist==null||title==null||year==null||imagePart==null){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Bad Request: Missing parameters");
            return;
        }
        Connection connection = null;
        long size=imagePart.getSize();
        long primaryKey=0;
        try  {
             connection = dataSource.getConnection();
            String insertQuery = "INSERT INTO albums (artist, title,year,size) VALUES (?,?,?,?)";
            PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
            insertStatement.setString(1, artist);
            insertStatement.setString(2, title);
            insertStatement.setString(3, year);
            insertStatement.setString(4, size+"");
            //insertStatement.setString(4, size+"");
            int rowsInserted = insertStatement.executeUpdate();
            if (rowsInserted > 0) {
                ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    String targetDirectory = "src/main/webapp/img";

                    primaryKey = generatedKeys.getLong(1); // get primary key
                    String fileName=serviceId+"_"+primaryKey+".jpg";
                    String path=targetDirectory;
                    String updateQuery = "UPDATE albums SET img=? WHERE id = ?";
                    PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
                    updateStatement.setString(1,path+fileName);
                    updateStatement.setString(2,primaryKey+"");
                    updateStatement.executeUpdate();
                    System.out.println("A new row has been inserted with ID: " + primaryKey);
                    OutputStream out = null;
                    try  {
                        InputStream fileContent = imagePart.getInputStream();
                        out = new FileOutputStream(new File(path+fileName));

                        int read;
                        final byte[] bytes = new byte[1024];
                        while ((read = fileContent.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }
                        fileContent.close();


                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            } else {
                System.out.println("Insertion failed.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        ResponseData responseData = new ResponseData();

        responseData.setImageSize(size+""); // Simulated file size
        responseData.setId(primaryKey+"");

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.println(responseData.toJson());
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String albumID = request.getPathInfo();
        if (albumID == null || albumID.length() <= 1) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        albumID = albumID.substring(1); // Remove the leading slash

        Connection connection = null;
        try {
            int id = Integer.parseInt(albumID);
            if (id < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            connection = dataSource.getConnection();
            String selectQuery = "SELECT * FROM albums  WHERE id = ?";
            PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
            selectStatement.setInt(1, id);
            ResultSet resultSet = selectStatement.executeQuery();

            ProfileData profileData=null ;
            if(resultSet.next()){
                profileData = new ProfileData();
                profileData.setArtist(resultSet.getString("artist"));
                profileData.setTitle(resultSet.getString("title"));
                profileData.setYear(resultSet.getString("year"));
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.println(profileData.toJson());
            }else {
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.println("Couldn't find the information");
            }

        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}


