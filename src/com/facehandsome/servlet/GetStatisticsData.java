package com.facehandsome.servlet;

import hadoop.similarPhoto.MdbSearchStatistic;
import hadoop.similarPhoto.MongoDBUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.facehandsome.bean.BarGraph;
import com.facehandsome.bean.Dataset;
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
		
		MdbSearchStatistic.searchResultStatistic();
		ArrayList<PieGraph> pieData = MongoDBUtil.findHandsomeProportion();
		BarGraph barData = MongoDBUtil.findRankedPics();
		
		/* debug
		ArrayList<Dataset> temp = barData.getDatasets();
		Dataset datset = new Dataset();
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(50);
		data.add(10);
		data.add(5);
		data.add(7);
		data.add(6);
		datset = temp.get(0);
		datset.setData(data);
		temp.remove(0);
		temp.add(datset);
		barData.setDatasets(temp);*/
		
		String[] res = new String[2];
		
		Gson gson = new Gson();
		res[0] = gson.toJson(pieData);
		res[1] = gson.toJson(barData);
		
		String json = gson.toJson(res);
		
		System.out.println("json = " + json);
		
		out.println(json);
	}

}
