package servlets;

import entities.User;
import utils.DBUtil;
import utils.MailSend;
import utils.Util;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "messageServlet", value="/message-servlet")
public class MessageServlet extends HttpServlet {

    @EJB
    private MailSend mailSender = new MailSend();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String senderMail = req.getParameter("pass_email");
        System.out.println("SENDER MAİLL:" +senderMail);
        // email isValid Control
        DBUtil dbUtil = new DBUtil();
        User user = dbUtil.mailIsValid(senderMail);

        String infoMessageCntrl = "";
        if(user != null){
            String strUuid = user.getUuid();
            String pathUrl = Util.base_url + "change-password?uuid=" + strUuid;

            // Mail Send Start  ***************************
            String fromEmail = "depoproject1@gmail.com";
            String username = "depoproject1";
            String password = "i0yrxfi1";

            String email = senderMail;
            boolean sendMessage = mailSender.sendEmail(fromEmail, username, password, email, pathUrl);
            //    boolean sendMessage = true;
            // Mail Send Finish **********
            infoMessageCntrl = "";
            if ( sendMessage ) {
                System.out.println("Mesaj Gönderildi");
                infoMessageCntrl = "1";
                resp.sendRedirect(Util.base_url + "index.jsp");
            }else {
                System.out.println("HATA! Mesaj Gönderilemedi!");
                infoMessageCntrl = "2";
            }
        }else{
            System.out.println("Girilen email sistemde mevcut değil");
            infoMessageCntrl = "3";
        }

        resp.setContentType("application/json");
        resp.getWriter().write(infoMessageCntrl);
    }
}
