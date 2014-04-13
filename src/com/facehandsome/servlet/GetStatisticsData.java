package com.facehandsome.servlet;

import hadoop.similarPhoto.MdbSearchStatistic;

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
		
		ArrayList<PieGraph> pieData = MdbSearchStatistic.getHandsomeProportion();
		BarGraph barData = new BarGraph();
		
		// pseudo data for debug
		ArrayList<String> labels = new ArrayList<String>();
		labels.add("1.bmp");
		labels.add("1317.bmp");
		barData.setLabels(labels);
		
		ArrayList<Dataset> datasets = new ArrayList<Dataset>();
		Dataset dataset = new Dataset();
		dataset.setFillColor("rgba(220,220,220,0.5)");
		dataset.setStrokeColor("rgba(220,220,220,1)");
		ArrayList<Integer> data = new ArrayList<Integer>();
		data.add(45);
		data.add(60);
		dataset.setData(data);
		datasets.add(dataset);
		barData.setDatasets(datasets);
		// end of pseudo data
		
		String[] res = new String[2];
		
		Gson gson = new Gson();
		res[0] = gson.toJson(pieData);
		res[1] = gson.toJson(barData);
		
		String json = gson.toJson(res);
		
		System.out.println("json = " + json);
		
		out.println(json);
	}

}
