package servlets;

import com.google.gson.Gson;
import entities.Customer;
import entities.OrderCustomerProduct;
import entities.Orders;
import entities.Products;
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
import java.util.List;

@WebServlet(name = "ordersServlet", value = { "/orders-post" , "/orders-get", "/orders-delete"})
public class OrdersServlet extends HttpServlet {
    SessionFactory sf = HibernateUtil.getSessionFactory();

    // orders-insert
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int oid = 0;
        Session sesi = sf.openSession();
        Transaction tr = sesi.beginTransaction();
        try {
            String obj = req.getParameter("obj");
            Gson gson = new Gson();
            Orders orders = gson.fromJson(obj, Orders.class);
            DBUtil dbUtil = new DBUtil();
            Products products = dbUtil.singleProduct( orders.getO_product());
            Customer customer = dbUtil.singleCustomer( orders.getO_customer());
            orders.setProduct(products);
            orders.setCustomer(customer);
            sesi.saveOrUpdate(orders);
            tr.commit();
            sesi.close();
            oid = 1;
        }catch ( Exception ex) {
            System.err.println("Save OR Update Error : " + ex);
        }finally {
            sesi.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write( "" +oid );
    }

    // orders-list
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int cid = Integer.parseInt(req.getParameter("cid"));
        Gson gson = new Gson();
        Session sesi = sf.openSession();
        List<OrderCustomerProduct> ls = sesi.createNativeQuery("select o.o_id, p.p_title, p.p_sale_price, o.o_amount, c.cu_name,c.cu_surname, o.o_receipt_number from Orders as o \n" +
                "inner join Customer as c on o.o_customer = c.cu_id \n" +
                "inner join Products as p on p.p_id = o.o_product \n" +
                "where o.o_customer = :cid and o.box_status=:ostatus")
                .setParameter("cid",cid)
                .setParameter("ostatus",0)
                .addEntity(OrderCustomerProduct.class)
                .getResultList();
        System.out.println(ls);
        sesi.close();
        String stJson = gson.toJson(ls);
        resp.setContentType("application/json");
        resp.getWriter().write( stJson );

    }

    // products-delete
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        int return_id = 0;
        Session sesi = sf.openSession();
        Transaction tr = sesi.beginTransaction();
        try {
            int o_id = Integer.parseInt( req.getParameter("o_id") );
            System.out.println(o_id);
            //get -> null geri donuyo yoksa load ise exception hatası geri donduruyor
            //Orders orders = sesi.get(Orders.class, o_id);
            Orders orders = sesi.load(Orders.class, o_id);
            System.out.println("Orders: "+ orders.getO_id());
            sesi.delete(orders);
            tr.commit();
            return_id = orders.getO_id();
        }catch (Exception ex) {
            System.err.println("Delete Error : " + ex);
        }finally {
            sesi.close();
        }

        resp.setContentType("application/json");
        resp.getWriter().write( ""+return_id );
    }

}
