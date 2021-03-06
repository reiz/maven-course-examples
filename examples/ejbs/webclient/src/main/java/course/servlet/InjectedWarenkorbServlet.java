package course.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import course.common.WarenkorbLocal;

@EJB(name = "course.common.Warenkorb", beanInterface = WarenkorbLocal.class)
public class InjectedWarenkorbServlet extends HttpServlet {

	/**
	 * Achtung: Es wird für jeden Request immer wieder ein neues WarenkorbBean
	 * zugewiesen.
	 */
	@EJB()
	private WarenkorbLocal warenkorb;

	private static final long serialVersionUID = 1364235522913699943L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println(this.toString() + " using SFSB " + warenkorb
				+ " processing request " + req);
		HttpSession session = req.getSession(true);
		String isLoggedOut = req.getParameter("logout");
		if (isLoggedOut != null) {
			warenkorb.abmelden();
			session.invalidate();
			return; // oder neuen Warenkorb erzeugen
		} else {
			String p = req.getParameter("produkt");
			if (p != null) {
				int a = Integer.parseInt(req.getParameter("anzahl"));
				warenkorb.kaufen(p, a);
			}
		}

		String url = "Injected.jsp";
		RequestDispatcher rd = req.getRequestDispatcher(url);
		rd.forward(req, resp);
	}

}
