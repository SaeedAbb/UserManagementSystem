package com.xAdmin.userMangment.servlet;

import com.xAdmin.userMangment.bean.User;
import com.xAdmin.userMangment.duo.UserDao;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * this servlet is the brain of the webAPP
 *
 * @author Saeed
 * @since 04.04.2021
 */
@WebServlet(name = "UserServlet", value = "/")
public class UserServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getServletPath();
        switch (action) {
            case "/new":
                showNewForm(request, response);
                break;
            case "/insert":
                insertUser(request, response);
                break;
            case "/delete":
                deleteUser(request, response);
                break;
            case "/edit":
                showEditForm(request, response);
                break;
            case "/update":
                updateUser(request, response);
                break;
            default:
                listUser(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }


    private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        dispatcher.forward(request, response);
    }

    //insert user
    private void insertUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User newUser = new User(request.getParameter("name"), request.getParameter("email"),
                request.getParameter("country"));
        userDao.insertUser(newUser);
        response.sendRedirect("list");
    }

    //delete user
    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        userDao.deleteUser(Integer.parseInt(request.getParameter("id")));
        response.sendRedirect("list");
    }

    //edit user
    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        User existingUser = userDao.selectUser(Integer.parseInt(request.getParameter("id")));
        RequestDispatcher dispatcher = request.getRequestDispatcher("user-form.jsp");
        request.setAttribute("user", existingUser);
        dispatcher.forward(request, response);
    }

    //update
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = new User(Integer.parseInt(request.getParameter("id")), request.getParameter("name"),
                request.getParameter("email"), request.getParameter("country"));
        userDao.updateUser(user);
        response.sendRedirect("list");
    }

    //default
    private void listUser(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        List<User> users = userDao.selectAllUsers();
        request.setAttribute("users", users);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("user-list.jsp");
        requestDispatcher.forward(request, response);

    }
}
