package servlets;

import com.google.gson.Gson;
import entities.ReceiptTotal;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "customerReceipt", value = "/customer-receipt-get")
public class CustomerReceipt extends HttpServlet {

    SessionFactory sf = HibernateUtil.getSessionFactory();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("DOGET CALISIO MUUUU");
        List<ReceiptTotal> ls=new ArrayList<>();
        Session sesi = sf.openSession();
        try {
            int cid = Integer.parseInt(req.getParameter("cid"));
            ls = sesi.createSQLQuery("CALL JoinTableReceipt(?)")
                    .setParameter(1,cid)
                    .addEntity(ReceiptTotal.class)
                    .getResultList();
        ls.forEach(itm->{
            System.out.println(itm.getO_receipt_number() + "sdsfscds");
            System.out.println(itm.getReceipt_total());
        });
        }catch (Exception ex){
            System.err.println("Error : " + ex);
        }finally {
            sesi.close();
        }
        Gson gson = new Gson();
        String stJson = gson.toJson(ls);
        resp.setContentType("application/json");
        resp.getWriter().write(stJson);


    }
}
