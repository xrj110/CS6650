package src;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/albums/*")
@MultipartConfig
public class AlbumServlet extends HttpServlet {
    private static long id = 10;
    private int getCount=0;
    private  int postCount=0;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        String artist = request.getParameter("artist");




        // Process the file upload if needed
        id++;
        ResponseData responseData = new ResponseData();
        responseData.setId(String.valueOf(id));
        responseData.setImageSize("3475"); // Simulated file size


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

        try {
            int id = Integer.parseInt(albumID);
            if (id < 1) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            ProfileData profileData = new ProfileData();
            profileData.setArtist("Sex Pistols");
            profileData.setTitle("Never Mind The Bollocks!");
            profileData.setYear("1997");

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.println(profileData.toJson());
        } catch (NumberFormatException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}

