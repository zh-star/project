package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/upload")
@MultipartConfig
public class UploadServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // 如果是批量上传
        for (Part part : req.getParts()) {
        }
        Part avatar = req.getPart("avatar");
        System.out.println(avatar.getName());
        System.out.println(avatar.getSubmittedFileName());
        System.out.println(avatar.getSize());
        System.out.println(avatar.getContentType());
        System.out.println(avatar.getInputStream());

        InputStream is = avatar.getInputStream();
        OutputStream os = new FileOutputStream("E:\\比特科技\\课程\\JavaWeb\\2019年-大四火箭班\\2020-01-16-博客系统\\上传上来的文件\\我的第一份文件.html");
        byte[] buf = new byte[4096];
        int len;

        while ((len = is.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        os.close();
    }
}
