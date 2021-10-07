package servlets;

import com.google.gson.Gson;
import entities.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.DBUtil;
import utils.HibernateUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "payInServlet", value = {"/payin-post" ,"/payin-get"})
public class PayInServlet extends HttpServlet {
    SessionFactory sf = HibernateUtil.getSessionFactory();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int pid = 0;
        Session sesi = sf.openSession();
        Transaction tr = sesi.beginTransaction();
        try {
            String obj = req.getParameter("obj");
            Gson gson = new Gson();
            PayIn payIn = gson.fromJson(obj, PayIn.class);
            payIn.setDate(LocalDateTime.now());
            System.out.println(payIn + "payinnnnn");
            Session sesi1 = sf.openSession();
            try {
                Receipt receipt = (Receipt) sesi1.createNativeQuery("select * from Receipt \n" +
                        "inner join Receipt_Orders on Receipt.receipt_id = Receipt_Orders.Receipt_receipt_id\n" +
                        "inner join Orders on Receipt_Orders.boxActions_o_id = Orders.o_id\n" +
                        "where Orders.o_receipt_number =?1")
                        .setParameter(1,payIn.getO_id())
                        .addEntity(Receipt.class)
                        .getSingleResult();
                receipt.setReceipt_payment(payIn.getPayment_amount());
                payIn.setReceipt(receipt);
            } catch (HibernateException e) {
                e.printStackTrace();
            }finally {
                sesi1.close();
            }
            sesi.saveOrUpdate(payIn);
            tr.commit();
            pid = 1;
        }catch ( Exception ex) {
            System.err.println("Save OR Update Error : " + ex);
        }finally {
            sesi.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write( "" +pid );
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cid = Integer.parseInt(req.getParameter("cid"));
        System.out.println(cid+"ciddddddd");
        Gson gson = new Gson();
        Session sesi = sf.openSession();

        List<PayInJoin> ls = null;
        try {
            ls = sesi.createNativeQuery("select * from Customer\n" +
                    "inner join Orders on Customer.cu_id = Orders.customer_cu_id\n" +
                    "inner join Receipt_Orders on Receipt_Orders.boxActions_o_id = Orders.o_id\n" +
                    "inner join Receipt on Receipt.receipt_id = Receipt_Orders.Receipt_receipt_id\n" +
                    "inner join PayIn on PayIn.receipt_receipt_id =  Receipt.receipt_id\n" +
                    "where Customer.cu_id= ?1 group by PayIn.payIn_id")
                    .setParameter(1,cid)
                    .addEntity(PayInJoin.class)
                    .getResultList();
        } catch (Exception e) {
            System.err.println("sql sorgusu error" + e);
        }
        System.out.println(ls);
        sesi.close();
        String stJson = gson.toJson(ls);
        resp.setContentType("application/json");
        resp.getWriter().write( stJson );

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int return_id = 0;
        Session sesi = sf.openSession();
        Transaction tr = sesi.beginTransaction();
        try {
            int payIn_id = Integer.parseInt( req.getParameter("payIn_id") );
            PayIn payIn = sesi.load(PayIn.class, payIn_id);
            sesi.delete(payIn);
            tr.commit();
            return_id = payIn.getPayIn_id();
        }catch (Exception ex) {
            System.err.println("Delete Error : " + ex);
        }finally {
            sesi.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write( ""+return_id );
    }
}
