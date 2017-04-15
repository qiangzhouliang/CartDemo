package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import dao.ItemsDAO;
import entity.Cart;
import entity.Items;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String action;//表示购物车的动作
	//商品业务逻辑类的对象
	private ItemsDAO idao = new ItemsDAO();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (request.getParameter("action")!= null) {
			this.action = request.getParameter("action");
			if (action.equals("add")) {
				if(addToCart(request,response)){
					request.getRequestDispatcher("./success.jsp").forward(request, response);
				}else {
					request.getRequestDispatcher("./failure.jsp").forward(request, response);
				}
			}
			if (action.equals("show")) {
				request.getRequestDispatcher("./cart.jsp").forward(request, response);
			}
			if (action.equals("delete")) {
				if (deleteFromCart(request,response)) {
					request.getRequestDispatcher("./cart.jsp").forward(request, response);
				}else {
					request.getRequestDispatcher("./cart.jsp").forward(request, response);
				}
			}
		}
	}

	//从购物车中删除商品
	private boolean deleteFromCart(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		Items item = idao.getItemsById(Integer.parseInt(id));
		if (cart.removeGoodsFromCart(item)) {
			return true;
		}else {
			return false;
		}
	}

	//添加商品进购物车
	private Boolean addToCart(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		String number = request.getParameter("num");
		Items item = idao.getItemsById(Integer.parseInt(id));
		
		//判断是否是第一次给购物车添加商品，需要给session中创建一个新的购物车对象
		if (request.getSession().getAttribute("cart") == null) {
			Cart cart = new Cart();
			request.getSession().setAttribute("cart", cart);
		}
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if (cart.addGoodsInCart(item, Integer.parseInt(number))) {
			return true;
		}else {
			return false;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
