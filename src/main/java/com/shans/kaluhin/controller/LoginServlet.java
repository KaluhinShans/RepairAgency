package com.shans.kaluhin.controller;

import com.shans.kaluhin.ProjectProperties;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebServlet(name = "Login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/login.jsp");
        view.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(!isCaptchaFill(req.getParameter("g-recaptcha-response"))){
            req.setAttribute("error", "captchaError");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            return;
        }

        boolean remember = req.getParameter("rememberMe") != null;

        String email = req.getParameter("email");
        String password = req.getParameter("password");

        if (UserService.isUserLogin(email, password)) {
            User user = UserService.getUserByEmail(email);

            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            session.setAttribute("lang", user.getLocale());

            Cookie sessionCookie = new Cookie("agency.user_id", String.valueOf(user.getId()));
            if (remember) {
                sessionCookie.setMaxAge(60 * 60 * 24 * 30);//30 days
            }
            resp.addCookie(sessionCookie);
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", UserService.error);
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }

    public static boolean isCaptchaFill(String recaptchaResponse){
        String googleCaptcha = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format(googleCaptcha, ProjectProperties.getProperty("recaptcha.secret"), recaptchaResponse)))
                    .build();

            HttpResponse<String> response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());

           return response.body().split(": ")[1].split(",")[0].equals("true");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

}
