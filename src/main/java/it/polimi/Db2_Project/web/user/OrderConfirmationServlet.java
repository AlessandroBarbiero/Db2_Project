package it.polimi.Db2_Project.web.user;

import it.polimi.Db2_Project.entities.OptionalProductEntity;
import it.polimi.Db2_Project.entities.OrderEntity;
import it.polimi.Db2_Project.entities.UserEntity;
import it.polimi.Db2_Project.entities.ValidityPeriodEntity;
import it.polimi.Db2_Project.services.UserService;
import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;

@WebServlet(name = "orderConfirmationServlet", value = "/confirmation")
public class OrderConfirmationServlet extends HttpServlet {
    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        if(session.getAttribute("pendingOrder") == null) {
            response.sendRedirect("home-user");
            return;
        }

        OrderEntity order = (OrderEntity) session.getAttribute("pendingOrder");
        ValidityPeriodEntity validityPeriod = order.getValidityPeriod();
        float totalCost;
        totalCost = validityPeriod.getMonthlyFee() * validityPeriod.getNumberOfMonths();
        for(OptionalProductEntity op : order.getOptionalProducts())
            totalCost+=op.getMonthlyFee() * validityPeriod.getNumberOfMonths();

        request.setAttribute("totalCost", totalCost);

        request.getRequestDispatcher("/UserPages/confirmation-page.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        OrderEntity order = (OrderEntity) session.getAttribute("pendingOrder");

        order.setValid(isValidPayment(req));
        order.setCreation(Timestamp.from(Instant.now()));
        UserEntity user = (UserEntity) session.getAttribute("user");
        order.setUser(user);

        float totalCost = Float.parseFloat(req.getParameter("totalCost"));

        order.setTotalPrice(totalCost);

        userService.createOrder(order);

        session.removeAttribute("pendingOrder");

        resp.sendRedirect("home-user");

    }

    private boolean isValidPayment(HttpServletRequest request){
        return "true".equals(request.getParameter("buy"));
    }
}
