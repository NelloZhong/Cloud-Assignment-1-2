var webSocket;
    var messages = document.getElementById("messages");
    var currentState=0;
    
    var readpoint=[];
	var map2;
	var map;
	var heatmap;
	var pointarray;
	var taxiData=[];
	var marker=[];
	var infowindow=[];
	function initialPoints(){
		

	      var mapOptions = {
	        zoom: 2,
	        //NYC
	        center: new google.maps.LatLng(40.7143528, -74.0059731),
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	      };
	      map = new google.maps.Map(document.getElementById('map-canvas'),
	          mapOptions);
	      
	      
	    
		  taxiData = ParseLocation(); 
	      var size=taxiData.length;
	      console.log("taxiData size " + size);
	      //Heatmap
	      heatmap = new google.maps.visualization.HeatmapLayer({
	      		data: taxiData
	      });
	      heatmap.setMap(map);
	      //Markers
	      var markers = [];
	      for (var i = 0; i < size; i++) {
	      		var latLng = taxiData[i];
	      		var marker = new google.maps.Marker({position: latLng, map: map});
	      		markers.push(marker);
	      }
	      console.log("Markers size "+ markers.length);
	     markerClusterer = new MarkerClusterer(map, markers, mcOptions);
		
//		  taxiData = ParseLocation(); 
//		if(taxiData.size()!=0){
//			for (var i = 0; i < size; i++) {
//	      		var latLng = taxiData[i];
//	      		var marker = new google.maps.Marker({position: latLng, map: map});
//	      		markers.push(marker);
//	      }
//		}
//	
//		var pointArray = new google.maps.MVCArray(taxiData);
//		heatmap = new google.maps.visualization.HeatmapLayer({
//		    data: pointArray
//		  });
//		var mcOptions = {gridSize: 50, maxZoom: 15};
//		//var mc = new MarkerClusterer(map);
//		var mc = new MarkerClusterer(map,marker,mcOptions);
//		heatmap.setMap(map2);
	}
	function clearMarker() {
		for (var i = 0; i < markers.length; i++) {
			marker[i].setMap(null);
		}
		marker=[];
	}
	function GetMap(){
		initialize();
		//mainMap();
	//	heatMap();
	//	initialPoints();
		openSocket();
		clearMarker();
	}
	
	
	
	function initialize() { 
		

	      var mapOptions = {
	        zoom: 2,
	        //NYC
	        center: new google.maps.LatLng(40.7143528, -74.0059731),
	        mapTypeId: google.maps.MapTypeId.ROADMAP
	      };
	      map = new google.maps.Map(document.getElementById('map-canvas'),
	          mapOptions);
	      
	      
	    
		  taxiData = ParseLocation(); 
	      var size=taxiData.length;
	      console.log("taxiData size " + size);
	      //Heatmap
	      heatmap = new google.maps.visualization.HeatmapLayer({
	      		data: taxiData
	      });
	      heatmap.setMap(map);
	      //Markers
	      var markers = [];
	      for (var i = 0; i < size; i++) {
	      		var latLng = taxiData[i];
	      		var marker = new google.maps.Marker({position: latLng, map: map});
	      		markers.push(marker);
	      }
	      console.log("Markers size "+ markers.length);
	     markerClusterer = new MarkerClusterer(map, markers, mcOptions);
	}
	
	
	function mainMap(){
		var latlng= new google.maps.LatLng(0,0);
		var myOptions={
				zoom: 1,
				center: latlng,
				mapTypeId: google.maps.MapTypeId.ROADMAP
		};
		var container = document.getElementById("mapContainer");
		map = new google.maps.Map(container,myOptions);
	}
	function heatMap(){
		var latlng= new google.maps.LatLng(0,0);
		var myOptions={
				zoom: 1,
				center: latlng,
				mapTypeId: google.maps.MapTypeId.SATELLITE
		};
		var container = document.getElementById("mapContainer2");
		map2 = new google.maps.Map(container,myOptions);
	}
	
	
	   function openSocket(){
	        // Ensures only one connection is open at a time
	        if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
	           writeResponse("WebSocket is already opened.");
	            return;
	        }
	        // Create a new instance of the websocket
	        webSocket = new WebSocket("ws://localhost:8080/");
	        /**
	         * Binds functions to the listeners for the websocket.
	         */
	        webSocket.onopen = function(event){
	            // For reasons I can't determine, onopen gets called twice
	            // and the first time event.data is undefined.
	            // Leave a comment if you know the answer.
	            if(event.data === undefined)
	                return;
	            writeResponse(event.data);
	        };
	        webSocket.onmessage = function(event){
	            //writeResponse(event.data);
	            if(currentState==0){
	            	currentState=1;
	            	readpoint[0]=event.data;
	            }else if(currentState==1){
	            	currentState=2;
	            	readpoint[1]=event.data;
	            }else if(currentState==2){
	            	currentState=3;
	            	readpoint[2]=event.data;
	            }else if(currentState==3){
	            	currentState=0;
	            	readpoint[3]=event.data;
	            	
	            	
	            	var tmpa=new google.maps.LatLng(readpoint[1],readpoint[2]);
	            	var tmpb = new google.maps.Marker({
						map: map,
						position: tmpa});
	           
	            	
	            	var tmpc = new google.maps.InfoWindow();
	            	tmpc.setContent(readpoint[3]+' : '+readpoint[0]);
					  google.maps.event.addListener(tmpb, 'click', function() {
						  tmpc.open(map, tmpb);
					  });
	            	
	            	
	            	
	            	var pointArray = new google.maps.MVCArray(tmpa);
	        		heatmap = new google.maps.visualization.HeatmapLayer({
	        		    data: pointArray
	        		  });
	        		//var mcOptions = {gridSize: 50, maxZoom: 15};
	        		//mc = new MarkerClusterer(map);
	        		//mc.add
	        		heatmap.setMap(map2);	
	            	
	            	
	            	
	            }
	        };
	        webSocket.onclose = function(event){
	            writeResponse("Connection closed");
	        };
	    }
	   
	    /**
	     * Sends the value of the text input to the server
	     */
	    function send(){
	        var text = document.getElementById("messageinput").value;
	        webSocket.send(text);
	        alert("Message is sent...");
	    }
	   
	    function closeSocket(){
	        webSocket.close();
	    }
	    function writeResponse(text){
	        messages.innerHTML += "<br/>" + text;
	        alert("Message is response...");
	    }
	   
		function buttonSubmitFunction(){
			
			
			
			
			infowindow=[];taxiData=[];
			var i=0;
			for(;i<marker.length;i++){
    			marker[i].setMap(null);
    		}
			marker=[];
			mainMap();
			heatMap();
			var text = document.getElementById("wordtobesearch").value;
			var option = document.createElement("option");
			option.text = text;
			document.getElementById("filter").add(option);
			if (text!=""){
				webSocket.send(text);
				document.getElementById("buttonSubmit").disabled=true;
				document.getElementById("stopSubmit").disabled=false;
				 alert("Message is sent...");
			}
		}
		
		function stopSubmitFunction(){
			webSocket.send("stop");
			infowindow=[];taxiData=[];marker=[];
			document.getElementById("buttonSubmit").disabled=false;
			document.getElementById("stopSubmit").disabled=true;
		}
		
		google.maps.event.addDomListener(window, 'load', GetMap);	