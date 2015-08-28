package com.francelabs.datafari.servlets.admin;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import com.francelabs.datafari.constants.CodesReturned;
import com.francelabs.datafari.service.db.DatabaseConstants;
import com.francelabs.datafari.user.User;

/**
 * Servlet implementation class getAllUsersAndRoles
 */
@WebServlet("/SearchAdministrator/isUserInBase")
public class IsUserInBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(IsUserInBase.class.getName());  
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsUserInBase() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject jsonResponse = new JSONObject();
		request.setCharacterEncoding("utf8");
		response.setContentType("application/json");
		try{
			if (request.getParameter(DatabaseConstants.USERNAMECOLUMN)!=null){
				User user = new User(request.getParameter(DatabaseConstants.USERNAMECOLUMN).toString(),"");
				int code = user.isInBase();
				String result = null;
				if (code == CodesReturned.TRUE){
					result = "true";
				}else if (code == CodesReturned.FALSE){
					result = "false";
				}
				if (code != CodesReturned.PROBLEMCONNECTIONDATABASE)
					jsonResponse.put("code", CodesReturned.ALLOK).put("statut", result);
				else
					jsonResponse.put("code", CodesReturned.PROBLEMCONNECTIONMONGODB).put("statut", "Problem with database");
		}else{
			jsonResponse.put("code", CodesReturned.PROBLEMQUERY).put("statut", "Problem with query");
		}
		}catch (JSONException e) {
			// TODO Auto-generated catch block
			logger.error(e);
		}
			PrintWriter out = response.getWriter();
			out.print(jsonResponse);
	}

}