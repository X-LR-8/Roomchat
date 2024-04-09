package org.example.Servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Addclasses.Room;
import org.example.Dao.Database;

import java.io.IOException;
import java.time.Instant;
import java.util.List;

@WebServlet("/room")
public class Roomservlet extends HttpServlet {
    private Database database;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        database=Database.getInstance();
        List<Room> list=database.read();
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(list);
        resp.getWriter().write(json);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        String roomname=req.getParameter("roomname");
        int maxmemb=Integer.parseInt(req.getParameter("maxmemb"));
        database=Database.getInstance();
        Room room=new Room(roomname,maxmemb);
        database.write(room);
    }
}
