$(document).ready(function() {


	var rowData = [];

	var rawData =      $('#cryptoData').data('crypto-data');

	if(rawData) {
		var dateString = rawData.date;
		var year = dateString.substring (0,4);
		var month = dateString.substring (4,6);
		var day = dateString.substring (6,8);
		varCurDate = new Date(year,month,day);
		rawData.quotes.forEach(myfunction);
	}

	function myfunction(item,index){
		var hour = item.time.substring(0,2);
		var min = item.time.substring(2,4);
		var pointTime = Date.UTC(year, month, day, hour,min);
		console.log('check - ' + new Date(pointTime));
		var aRow = [pointTime, parseFloat(item.price)];
		rowData.push(aRow);

	}

	var xaxis= {
			mode: "time",
			timeBase: "milliseconds",
			timeformat: "%H:%M"
	};

	var options = {xaxis : xaxis};

	if(rowData && rowData.length > 0) {
		$.plot($("#cryptoChart"), [  rowData ], options );
	}

});