package servlets;

import com.google.gson.Gson;
import entities.OrderCustomerProduct;
import entities.Products;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "amountServlet", value = "/amount-servlet")
public class AmountServlet extends HttpServlet {
    SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int selectedProduct = Integer.parseInt(req.getParameter("selectedProduct"));
        Gson gson = new Gson();
        Session sesi = sf.openSession();
        Products products = (Products) sesi.createNativeQuery("select * from Products where p_id=?1")
                .setParameter(1,selectedProduct)
                .addEntity(Products.class)
                .getSingleResult();
        System.out.println(products +"Adet ayarı çalışıyor mu??");
        sesi.close();
        String stJson = gson.toJson(products);
        resp.setContentType("application/json");
        resp.getWriter().write( stJson );

    }
}
