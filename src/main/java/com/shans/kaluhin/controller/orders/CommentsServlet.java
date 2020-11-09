package com.shans.kaluhin.controller.orders;

import com.shans.kaluhin.entity.Comment;
import com.shans.kaluhin.entity.User;
import com.shans.kaluhin.service.OrderService;
import com.shans.kaluhin.service.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CommentsServlet")
public class CommentsServlet extends HttpServlet {
    int totalComments = 5;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        pagination(req);
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/jsp/orders/comments.jsp");
        view.forward(req, resp);
    }

    private void pagination(HttpServletRequest req) {
        User user = (User) req.getSession().getAttribute("user");
        String sId = req.getParameter("id");
        String spage = req.getParameter("page");
        int page = 1;

        if (spage != null) {
            page = Integer.parseInt(spage);
        }

        if(sId != null){
            UserService userService = new UserService();
            user = userService.getUserByID(Integer.parseInt(sId));
        }

        int startPosition = page * totalComments - totalComments;
        req.setAttribute("page", page);

        OrderService orderService = new OrderService();
        List<Comment> comments = orderService.getCommentsByMaster(user.getId(), startPosition, totalComments);

        int nOfPages = orderService.getNumberOfRows() / totalComments;

        if (nOfPages % totalComments > 0) {
            nOfPages++;
        }

        req.setAttribute("comments", comments);

        req.setAttribute("nOfPages", nOfPages);

        req.setAttribute("master", user);
    }
}
