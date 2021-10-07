package utils;

import entities.*;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DBUtil {

    SessionFactory sf = HibernateUtil.getSessionFactory();
    public int login(String email, String password, String remember, HttpServletRequest req, HttpServletResponse resp) {

        Session sesi = sf.openSession();
        List<User> ls = null;
        try {
            String sql = "from User where email=?1 and password=?2";
            ls = sesi
                .createQuery(sql)
                .setParameter(1, email)
                .setParameter(2,Util.MD5(password))
                .getResultList();
            System.out.println("status : " + ls.size());
            // COOKIE
            if(ls.size() !=0){
                int uid = ls.get(0).getUid();
                String name = ls.get(0).getName();

                req.getSession().setAttribute("uid", uid);
                req.getSession().setAttribute("name", name);

                if ( remember != null && remember.equals("on")) {
                    name = name.replaceAll(" ", "_");
                    String val = uid+"_"+name;
                    Cookie cookie = new Cookie("admin", val);
                    cookie.setMaxAge( 60*60 );
                    resp.addCookie(cookie);
                }
            }
        } catch (Exception e) {
            System.err.println("Login Error : " + e);
        } finally {
            sesi.close();
        }
      
        return ls.size();
    }


    public boolean sendMessage(User user){
        boolean status = false;
        DB db = new DB();
        try {
            String sql = "select password from User where uid=?";
            PreparedStatement pre = db.conn.prepareStatement(sql);
            pre.setInt(1, user.getUid());
            ResultSet rs = pre.executeQuery();
            status = rs.next();
        } catch (SQLException e) {
            System.err.println("sendMessageError : " + e);
        }finally {
            db.close();
        }
        return status;
    }


    // all Customer
    public List<Customer> allCustomers(){
        Session sesi = sf.openSession();
        List<Customer> ls = sesi.createQuery("from Customer ").getResultList();
        sesi.close();
        return ls;
    }

    // all Products
    public List<Products> allProduct(){
        Session sesi = sf.openSession();
        List<Products> ls = sesi.createQuery("from Products ").getResultList();
        sesi.close();
        return ls;
    }

    // customerSearch procedure
    public List<Customer> customerSearch(String search){
        Session sesi = sf.openSession();
        List<Customer> customerSearch = sesi.createSQLQuery("CALL customerSearch(?)")
                .addEntity(Customer.class)
                .setParameter(1,search)
                .getResultList();
        customerSearch.forEach(itm->{
            System.out.println(itm);
        });
        sesi.close();
        return customerSearch;
    }
    public List<Orders> boxActionsList(long receipt){
        Session sesi = sf.openSession();
        return sesi.createQuery("from Orders where o_receipt_number = ?1")
                .setParameter(1,receipt)
                .getResultList();
    }

    // single Customer
    public Customer singleCustomer(int cid){
        Customer cus = new Customer();
        try{
            Session sesi = sf.openSession();
            cus = sesi.find(Customer.class,cid);
            sesi.close();
        }catch (Exception ex){
            System.err.println("singleCustomer Error : " + ex);
        }
        return cus;
    }

    // single Product
    public Products singleProduct(int pid){
        Products pro = new Products();
        try {
            Session sesi = sf.openSession();
            pro = sesi.find(Products.class,pid);
            sesi.close();
        }catch (Exception ex){
            System.err.println("singleProduct Error : " + ex);
        }
        return pro;
    }


    public List<Receipt> allReceipt() {
        Session sesi = sf.openSession();
        List<Receipt> ls = sesi.createQuery("from Receipt").getResultList();
        sesi.close();
        return ls;
    }


    public User mailIsValid(String email){
        System.out.println(email + "emailemailemail");
        Session sesi = sf.openSession();
        User user = null;
        try {
            String sql = "from User Where email = ?1";

            user = (User) sesi
                    .createQuery(sql)
                    .setParameter(1, email)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("mailIsValid Error : " + e);
        }finally {
            sesi.close();
        }
        return user;
    }


    public User getUuidUser(String uuid){
        Session sesi = sf.openSession();
        User user = null;
        try {
            String sql = "from User where uuid = ?1";

            user = (User) sesi
                    .createQuery(sql)
                    .setParameter(1, uuid)
                    .getSingleResult();
        } catch (Exception e) {
            System.err.println("uuid Error : " + e);
        }finally {
            sesi.close();
        }
        return user;
    }

}
