<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="description" content="for cloud project">
		<meta name="description" content="for cloud project">
		<meta name="keywords" content="web ftont, HTML5, ajax, javascript, image upload">
		<meta name="author" content="student37">
		<link rel="stylesheet" href="./Resource/demo.css" type="text/css">
		<link rel="stylesheet" type="text/css" href="./Resource/css/demo.css" />
		<link rel="stylesheet" type="text/css" href="./Resource/css/style.css" />
		<link rel="stylesheet" type="text/css" href="./Resource/css/jquery.jscrollpane.css" media="all" />
		<link href='http://fonts.googleapis.com/css?family=PT+Sans+Narrow&v1' rel='stylesheet' type='text/css' />
		<link href='http://fonts.googleapis.com/css?family=Coustard:900' rel='stylesheet' type='text/css' />
		<link href='http://fonts.googleapis.com/css?family=Rochester' rel='stylesheet' type='text/css' />
		<title>Statistics Page</title>
	</head>
	<body>
	
	
	<div id="header">
    <h3>HANDSOME &nbsp;&nbsp; FACE &nbsp;&nbsp; Statistics</h3>
	</div>
	<div id="main">
		<h1>Face Comparison based on Cluster</h1>
	    <div id="body" class="light">
	    	<div id="content">
	        	<h3><button id="switch">Bar Graph</button></h3>
	            <div class="article_new"><a href="index.jsp">Back</a></div>
	            <div id="statistics_pie">
	            	<canvas id="pie_chart" width="400" height="400"></canvas>
	            	<div id="colormean"></div>
	            </div>
	            <div id="statistics_bar" style="display:none">
	            	<canvas id="bar_chart" width="400" height="400"></canvas>
	            	<div id="image_content"></div>
	            </div>
	        </div>       
	    </div>
	</div>
	<div id="footer">
		<%@ include file="hostname.jsp" %><br>
	    Designed by Lin Ji, Niu ZhiXiong, Xu ZuLiang<br>
	    Powered by <a href="http://www.cs.hku.hk/">HKUCS Cluster</a><br>
	    Copyright© HKU<br>    
	</div>
	
	
	<script src="Resource/js/Chart.js"></script>
	<script src="Resource/jquery-2.1.0.min.js"></script>
	<script type="text/javascript">
		var ctx = $("#pie_chart").get(0).getContext("2d");
		var ctx2 = $("#bar_chart").get(0).getContext("2d");
		var pieChart = new Chart(ctx);
		var barChart = new Chart(ctx2);
		var pieData;
		var barData;
		
		$(function(){
			$.post("getstatisticsdata", function(data) {
				var json = $.parseJSON(data);
				
				pieData = $.parseJSON(json[0]);
				barData = $.parseJSON(json[1]);
				
				if (pieData.length != 0) {
					/* start of pie chart*/
					var colors = new Array(pieData.length);
					var colorText = new Array(pieData.length);
					colorText[0] = "Not Handsome";
					colorText[1] = "Handsome";
					
					var percents = new Array(pieData.length);
					percents[0] = parseInt(pieData[0].value/(pieData[0].value + pieData[1].value) * 100);
					percents[1] = 100 - percents[0];
					
					// get the colors
					for (var i = 0; i < colors.length; i++) {
						colors[i] = pieData[i].color;
						$("#colormean").append("<div class=\"color\" style=\"background:" + colors[i] + "\">" + colorText[i] + " " + percents[i] + "%</div>");
					}

					// show pie graph
					pieChart.Pie(pieData);
					/* end of pie chart*/
					
					
					/* start of bar chart*/
					var labels = barData.labels;
					
					for (var i = 0; i < labels.length; i++) {
						$("#image_content").append("<div class=\"limit_size\"><img src=\"ximages/" + labels[i] + "\" /><p>" + labels[i] + "</p></div>");
					}
					/* end of bar chart*/
					
				} else {
					$("#statistics_pie").html("<h3>NO &nbsp;&nbsp; DATA</h3>");
					$('#switch').attr('disabled',true);
				}
			});
			
			$("#switch").click(function(){
				if ($(this).text() == "Bar Graph") {
					$(this).text("Pie Graph");
					$("#statistics_pie").hide();
					$("#statistics_bar").fadeIn();
					barChart.Bar(barData);
				} else {
					$(this).text("Bar Graph");
					$("#statistics_bar").hide();
					$("#statistics_pie").fadeIn();
					pieChart.Pie(pieData);
				}
			});
			
		});
		
	</script>
	</body>
</html>