package servlets;

import com.google.gson.Gson;
import entities.PayOut;
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

@WebServlet(name = "payOutSearchServlet", value = "/payout-search-get")
public class PayOutSearchServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String ara = req.getParameter("search").trim().replaceAll(" +", "") + "*";
        ara="*"+ara;
        SessionFactory sf = HibernateUtil.getSessionFactory();
        Session sesi = sf.openSession();
        List<PayOut> ls = new ArrayList<>();
        try {
            if (ara.equals("*")) {
                ls = sesi.createQuery("from PayOut ").getResultList();

            } else {
                ls = sesi.createSQLQuery("CALL payOutSearch(?)")
                        .addEntity(PayOut.class)
                        .setParameter(1, ara)
                        .getResultList();
                System.out.println("Arama : " + ara);
            }
        } catch (Exception e) {
            System.err.println("payOutSearchServlet doGet : " + e);
        } finally {
            sesi.close();
        }

        Gson gson = new Gson();
        String stJson = gson.toJson(ls);
        resp.setContentType("application/json");
        resp.getWriter().write(stJson);




    }
}
