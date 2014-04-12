package com.facehandsome.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.facehandsome.bean.PieGraph;
import com.google.gson.Gson;

/**
 * Servlet implementation class GetStatisticsData
 */
@WebServlet("/GetStatisticsData")
public class GetStatisticsData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetStatisticsData() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		PieGraph data1 = new PieGraph(30, "#F38630");
		PieGraph data2 = new PieGraph(50, "#E0E4CC");
		PieGraph data3 = new PieGraph(100, "#69D2E7");
		
		ArrayList<PieGraph> list = new ArrayList<PieGraph>();
		list.add(data1);
		list.add(data2);
		list.add(data3);
		
		Gson gson = new Gson();
		String json = gson.toJson(list);
		
		System.out.println("json = " + json);
		
		out.println(json);
	}

}
