package bin.search.web.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bin.search.util.PropertyConfiguration;

public class ShowPicServlet extends HttpServlet {
	
	private static final String path = PropertyConfiguration.getProductImageDir();

	protected void service(HttpServletRequest req, HttpServletResponse rep)
			throws ServletException, IOException {

		String id = req.getParameter("id");
		if (id == null) {
			return;
		}

		OutputStream os = rep.getOutputStream();
		rep.setContentType("image/jpeg");

		File f = new File(new File(path), id);
		InputStream is = new FileInputStream(f);

		byte[] bs = new byte[512];
		int length = 512;
		while ((length = is.read(bs)) != -1) {
			os.write(bs, 0, length);
		}

		os.flush();

	}

}

