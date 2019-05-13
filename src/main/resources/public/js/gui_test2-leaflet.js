function Map(info) {
	// Create variables to be used by listeners
	var maplet = this;
	this.map;
	this.geojsonLayer;
	this.districtLayer;
	this.precinctLayer;
	this.infoCtrl;
	this.layers;
	this.layerCtrl;
	maplet.GeoDataService;
	maplet.$mdToast;
	maplet.stateCallback;
	maplet.colorIndex;
	maplet.colorQueue = [];
	this.leaf_states = {};
	this.uiInfo = info;

	this.mapSetup = function() {
		geoSetup();
		infoPanelSetup();
	};

	maplet.removeStateLayer = function() {
		maplet.map.removeLayer(maplet.geojsonLayer);
		maplet.layerCtrl.removeLayer(maplet.geojsonLayer);
	};

	maplet.getState = function(stateId) {
		return maplet.leaf_states[stateId];
	};

	maplet.initDistricts = function(districtGeoJSON) {
		maplet.genColorList(districtGeoJSON.features.length);
		maplet.districtLayer = new L.geoJSON(districtGeoJSON, {
			style: districtStyle,
			onEachFeature: onEachDistrict
		});
		maplet.layerCtrl.addOverlay(maplet.districtLayer, 'Current Congressional Districts');
		maplet.layers.addLayer(maplet.districtLayer);
	};

	maplet.initPrecincts = function(precinctGeoJSON) {
		maplet.precinctLayer = new L.geoJSON(precinctGeoJSON, {
			style: precinctStyle,
			onEachFeature: onEachPrecinct
		});
		maplet.layerCtrl.addOverlay(maplet.precinctLayer, 'Precincts');
		maplet.layers.addLayer(maplet.precinctLayer);
		maplet.removeStateLayer();
	};

	maplet.initClusters = function() {

	};

	/**
	 * Helper functions
	 */
	// Style Functions
	maplet.genColorList = function(numDistricts) {
		maplet.colorIndex = 0;
		for (var i = 0; i < 360; i += (360 / numDistricts)) {
			var color = {};
			color.hue = (i).toString();
			color.sat = (90 + Math.round(Math.random() * 10)).toString() + '%';
			color.light = (50 + Math.round(Math.random() * 10)).toString() + '%';
			maplet.colorQueue.push(color);
		}
	};

	function stateStyle(feature) {
	    return {
	        fillColor: '#9e42f4',
	        weight: 2,
	        opacity: 1,
	        color: '#7330b2',
	        fillOpacity: 0.4
	    };
	}

	function districtStyle(feature) {
		var colorSettings;
		if (maplet.colorQueue.length > 0) {
			colorSettings= maplet.colorQueue.splice(maplet.colorIndex, 1)[0];
			var queueLength = maplet.colorQueue.length;
			maplet.colorIndex = (maplet.colorIndex + Math.floor(queueLength / 2)) % queueLength;
		} else {
			colorSettings = { hue: 0, sat: 0, light: 0 };
		}
		var color = "hsl(" + colorSettings.hue + ", " +
							 colorSettings.sat + ", " +
							 colorSettings.light + ")";
		console.log(feature);
		console.log(color);
		return {
			fillColor: color,
			weight: 2,
			opacity: 1,
			color: color,
			fillOpacity: 0.4
		};
	}

	function precinctStyle(feature) {
		return {
			fillColor: '#aaa',
			weight: 1,
			opacity: 1,
			color: '#aaa',
			fillOpacity: 0
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
		// Place selector (based on type of feature)
	    maplet.infoCtrl.update();
	}

	// Zooms map to fit state
	maplet.zoomToState = function(e) {
		var layer = e.target;
	    maplet.uiInfo.selectedState = layer.feature.properties.NAME;
		var bounds = e.target.getBounds();
		var SHIFT = 0.5;
		bounds = bounds.toBBoxString(); // 'southwest_lng,southwest_lat,northeast_lng,northeast_lat'
		bounds = bounds.split(",");
		for(var i = 0; i < bounds.length; i++){
			bounds[i] = parseFloat(bounds[i]);
		}
		bounds = [[bounds[1],bounds[0]+SHIFT],[bounds[3],bounds[2]+SHIFT]];
	    maplet.map.fitBounds(bounds);
	    if (maplet.stateCallback) {
			maplet.stateCallback(layer.feature.properties.id, maplet);
		}
	};

	// Maps listener functions to various layer
	function onEachState(feature, layer) {
	    layer.on({
	        mouseover: highlightFeature,
	        mouseout: resetHighlight,
	        click: maplet.zoomToState
	    });
	}

	function onEachDistrict(feature, layer) {
		/*layer.on({
			mouseover: highlightFeature,
			mouseout: resetHighlight
		});*/
	}

	function onEachPrecinct(feature, layer) {
		/*layer.on({
			mouseover: highlightFeature,
			mouseout: resetHighlight
		});*/
	}

	/**
	 * Initial Setup
	 */
	 function geoSetup() {
		// Loading the JSON file
		maplet.geojsonLayer = new L.geoJSON(maplet.uiInfo.states, {
			style: stateStyle,
			onEachFeature: onEachState
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

		maplet.layers = L.layerGroup().addTo(maplet.map);
		maplet.layers.addLayer(maplet.geojsonLayer);
		var startOverlay = { 'States': maplet.geojsonLayer};
		maplet.layerCtrl = L.control.layers({ "greymap": tileLayer },
											startOverlay,
											{ hideSingleBase: true, position: 'topleft' });
		maplet.layerCtrl.addTo(maplet.map);

		tileLayer.addTo(maplet.map);
		//maplet.geojsonLayer.addTo(maplet.map);

		maplet.geojsonLayer.eachLayer(function(layer) {
			if (layer.feature) {
				maplet.leaf_states[layer.feature.properties.id] = layer._leaflet_id;
			}
		});
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