package servlets;

import com.google.gson.Gson;
import entities.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.DBUtil;
import utils.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "customerSearchServlet", value = "/customer-search-get")
public class CustomerSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ara = req.getParameter("search").trim().replaceAll(" +", "") + "*";
        ara="*"+ara;
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesi = sf.openSession();
        List<Customer> ls = new ArrayList<>();
        try {
            if (ara.equals("*")) {
                ls = sesi.createQuery("from Customer").getResultList();

            } else {
                ls = sesi.createSQLQuery("CALL customerSearch(?)")
                        .addEntity(Customer.class)
                        .setParameter(1, ara)
                        .getResultList();
                System.out.println("Arama : " + ara);
            }
        } catch (Exception e) {
            System.err.println("customerSearchServlet doGet : " + e);
        } finally {
            sesi.close();
        }

        Gson gson = new Gson();
        String stJson = gson.toJson(ls);
        resp.setContentType("application/json");
        resp.getWriter().write(stJson);




    }
}
