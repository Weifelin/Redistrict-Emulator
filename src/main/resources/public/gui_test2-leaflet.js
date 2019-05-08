function Map(info) {
	// Create variables to be used by listeners
	this.map;
	this.geojsonLayer;
	this.infoCtrl;
	var maplet = this;
	this.leaf_states = [];
	this.uiInfo = info;

	this.mapSetup = function() {
		geoSetup();
		infoPanelSetup();
	}

	/**
	 * Helper functions
	 */
	// Style Function
	function style(feature) {
	    return {
	        fillColor: '#9e42f4',
	        weight: 2,
	        opacity: 1,
	        color: '#7330b2',
	        fillOpacity: 0.4
	    };
	}

	// Thickens state boundary on hover
	function highlightFeature(e) {
	    var layer = e.target;

	    layer.setStyle({
	        weight: 5,
	        color: '#666',
	        dashArray: '',
	        fillOpacity: 0.4
	    });

	    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
	        layer.bringToFront();
	    }

	    //  Update Info control with highlighted state's property
	    maplet.infoCtrl.update(layer.feature.properties);
	}

	// Resets state to original style
	function resetHighlight(e) {
	    maplet.geojsonLayer.resetStyle(e.target);
	    // Remove state info
	    maplet.infoCtrl.update();
	}

	// Zooms map to fit state
	function zoomToFeature(e) {
		var layer = e.target;
	    maplet.uiInfo.selectedState = layer.feature.properties.NAME;
		var bounds = e.target.getBounds();
		var SHIFT = 2.5;
		bounds = bounds.toBBoxString(); // 'southwest_lng,southwest_lat,northeast_lng,northeast_lat'
		bounds = bounds.split(",");
		for(var i = 0; i < bounds.length; i++){
			bounds[i] = parseFloat(bounds[i]);
		}
		bounds = [[bounds[1],bounds[0]+SHIFT],[bounds[3],bounds[2]+SHIFT]];
	    maplet.map.fitBounds(bounds);
	}

	// Maps listener functions to state layer
	function onEachFeature(feature, layer) {
		maplet.leaf_states.push(feature.properties.NAME);
	    layer.on({
	        mouseover: highlightFeature,
	        mouseout: resetHighlight,
	        click: zoomToFeature
	    });
	}

	/**
	 * Initial Setup
	 */
	 function geoSetup() {
		// Loading the JSON file
		maplet.geojsonLayer = new L.GeoJSON.AJAX("gz_2010_us_040_00_500k.json", {
		    style: style,
		    onEachFeature: onEachFeature
		}); 

		/**
		 * Leaflet Map
		 */
		var mapboxAccessToken = "sk.eyJ1IjoiZHJzZWNhbnQiLCJhIjoiY2pzOW4yZm8zMGVhNTRhcno5OGUzOHZpeiJ9.q5a-WOex7gnG3-BhKbs5XQ";
		maplet.map = L.map('map').setView([37.8, -96], 4);

		var tileLayer = L.tileLayer('https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=' + mapboxAccessToken, {
		    id: 'mapbox.light',
		    attribution: 'Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, Imagery © <a href="https://www.mapbox.com/">Mapbox</a>'
		});

		// Ensures that tiles load
		tileLayer.on("load",function() { 
			setTimeout(() => {
     			maplet.map.invalidateSize();
   			}, 0);
		});

		tileLayer.addTo(maplet.map);

		maplet.geojsonLayer.addTo(maplet.map);
	}

	/**
	 * State Info Panel
	 */
	function infoPanelSetup() {
		maplet.infoCtrl = L.control();
		maplet.infoCtrl.setPosition('bottomleft');

		maplet.infoCtrl.onAdd = function (map) {
		    this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
		    this.update();
		    return this._div;
		};

		// Update info control based on feature properties
		maplet.infoCtrl.update = function (props) {
		    this._div.innerHTML = '<h4>State Metrics</h4>' +  (props ?
		        '<i>' + props.NAME + '</i><br />' + props.CENSUSAREA + ' people / mi<sup>2</sup>' +
		        '<br />' + props.STATE + ' districts' 
		        : 'Hover over a state');
		    this._div.innerHTML += '<br>Zoom: ' + maplet.map.getZoom();
		};

		maplet.infoCtrl.addTo(maplet.map);
	}
}