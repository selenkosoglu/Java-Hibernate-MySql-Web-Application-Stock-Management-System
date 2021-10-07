package servlets;

import com.google.gson.Gson;
import entities.*;
import props.JoinReceipt;
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

@WebServlet(name = "receiptServlet", value = {"/receipt-get","/receipt-post"})
public class ReceiptServlet extends HttpServlet {

    SessionFactory sf = HibernateUtil.getSessionFactory();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String obj = req.getParameter("objx");
        Gson gson = new Gson();
        JoinReceipt joinReceipt = gson.fromJson(obj, JoinReceipt.class);
        Session sesiUpdateStatus = sf.openSession();
        Transaction tr1 = sesiUpdateStatus.beginTransaction();
        int bid = 0;
        try {
            System.out.println(joinReceipt.getO_receipt_number());
            sesiUpdateStatus.createSQLQuery("CALL boxStatusChange(:data)")
                    .setParameter("data",joinReceipt.getO_receipt_number())
                    .executeUpdate();
            tr1.commit();
            bid = 1;
        }
        catch (Exception ex){
            System.err.println("Update Statue Error : " + ex);
        }finally {
            sesiUpdateStatus.close();
        }
        if(bid == 1) {
            Session sesi = sf.openSession();
            Transaction tr = sesi.beginTransaction();
            try {
                DBUtil dbUtil = new DBUtil();
                List<Orders> orders = dbUtil.boxActionsList(joinReceipt.getO_receipt_number());
                int total = 0;
                if (orders != null) {
                    for (Orders item : orders) {
                        total = total + (item.getProduct().getP_sale_price() * item.getO_amount());
                    }
                    Receipt receipt = new Receipt();


                    receipt.setReceipt_total(total);
                    receipt.setDate(LocalDateTime.now());
                    receipt.setBoxActions(orders);
                    receipt.setReceipt_payment(0);

                    bid = (int) sesi.save(receipt);

                    tr.commit();

                }

            } catch (Exception ex) {
                System.err.println("Save Or Update Error : " + ex);
            } finally {
                sesi.close();
            }
        }



        resp.setContentType("application/json");
        resp.getWriter().write(""+bid);





    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ReceiptTotal receiptTotal = new ReceiptTotal();
        System.out.println("doget calısıor mu");
        Session sesi = sf.openSession();
        try {
            int cid = Integer.parseInt(req.getParameter("cid"));
            int oid = Integer.parseInt(req.getParameter("o_receipt_number"));
            receiptTotal = (ReceiptTotal) sesi.createNativeQuery("SELECT DISTINCT o_receipt_number, receipt_total, receipt_payment  FROM Receipt as r\n" +
                    "INNER JOIN Receipt_Orders as ro\n" +
                    "on r.receipt_id = ro.Receipt_receipt_id\n" +
                    "INNER JOIN Orders as o\n" +
                    "on o.o_id = ro.boxActions_o_id\n" +
                    "INNER JOIN Customer as c\n" +
                    "on c.cu_id = o.customer_cu_id\n" +
                    "WHERE cu_id =?1 and o_receipt_number=?2")
                            .setParameter(1,cid)
                            .setParameter(2,oid)
                    .addEntity(ReceiptTotal.class)
                                    .getSingleResult();


        }catch (Exception ex){
            System.err.println("Error : " + ex);
        }finally {
            sesi.close();
        }
        Gson gson = new Gson();
        String stJson = gson.toJson(receiptTotal);
        resp.setContentType("application/json");
        resp.getWriter().write(stJson);



    }
}
