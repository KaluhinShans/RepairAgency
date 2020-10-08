package com.shans.kaluhin.controller;

import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@WebServlet(name = "Profile")
@MultipartConfig
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
      if(uri.equals("/profile/edit")){
          RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/profileEdit.jsp");
          view.forward(req, resp);
          return;
      }
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part filePart = req.getPart("photo");
        User user = (User) req.getSession(false).getAttribute("user");
        if (filePart != null) {
            savePhoto(user, filePart);
        }
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/profile.jsp");
        view.forward(req, resp);
    }

    private void savePhoto(User user, Part filePart) {
        String name = user.getEmail() + ".jpg";
        File file = new File(ProjectProperties.getProperty("images") + name);

        try (BufferedInputStream bis = new BufferedInputStream(filePart.getInputStream());
             FileOutputStream fos = new FileOutputStream(file)) {

            int ch;
            while ((ch = bis.read()) != -1) {
                fos.write(ch);
            }

            UserService.savePhoto(user, name);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
