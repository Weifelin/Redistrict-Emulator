function Map(info) {
	// Create variables to be used by listeners
	var maplet = this;
	this.map;
	this.geojsonLayer;
	this.districtLayer;
	this.precinctLayer;
	this.stateInfoCtrl;
	this.layers;
	this.layerCtrl;
	maplet.GeoDataService;
	maplet.$mdToast;
	maplet.stateCallback;
	maplet.colorIndex;
	maplet.colorQueue = [];
	maplet.disStyleMap = {};
	this.leaf_states = {};
	this.leaf_precincts = {};
	this.leaf_clusters = {};
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
	maplet.getPrecinct = function(precinctId) {
		return maplet.leaf_precincts[precinctId];
	};
	maplet.getCluster = function(clusterId) {
		return maplet.leaf_clusters[clusterId];
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
		maplet.precinctLayer.eachLayer(function(layer) {
			if (layer.feature) {
				maplet.leaf_precincts[layer.feature.properties.precinctID] = layer._leaflet_id;
			}
		});
	};

	maplet.initClusters = function() {
		var decimalPrecision = 6;
		var initialGeoJSON = maplet.precinctLayer.toGeoJSON(decimalPrecision);
		maplet.clusterLayer = new L.geoJSON(initialGeoJSON, {
			style: precinctStyle,
			onEachFeature: onEachPrecinct
		});
		hidePrecinctPanel();
		maplet.layerCtrl.addOverlay(maplet.clusterLayer, 'Clusters');
		maplet.layers.addLayer(maplet.clusterLayer);
		maplet.clusterLayer.eachLayer(function(layer) {
			if (layer.feature) {
				maplet.leaf_clusters[layer.feature.properties.precinctID] = layer._leaflet_id;
			}
		});
	};

	/**
	 * Helper functions
	 */
	// Style Functions
	maplet.genColorList = function(numDistricts) {
		maplet.colorIndex = 0;
		var shift = Math.floor(Math.random() * 360);
		for (var i = shift; i < (360 + shift); i += (360 / numDistricts)) {
			var color = {};
			color.hue = (i % 360).toString();
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
		var style;
		if (maplet.colorQueue.length > 0) {
			colorSettings= maplet.colorQueue.splice(maplet.colorIndex, 1)[0];
			var queueLength = maplet.colorQueue.length;
			maplet.colorIndex = (maplet.colorIndex + Math.floor(queueLength / 2)) % queueLength;

			var color = "hsl(" + colorSettings.hue + ", " +
				colorSettings.sat + ", " +
				colorSettings.light + ")";
			style = {
				fillColor: color,
				weight: 2,
				opacity: 1,
				color: color,
				fillOpacity: 0.4
			};
			maplet.disStyleMap[feature.properties.DISTRICT] = style;
		} else {
			style = maplet.disStyleMap[feature.properties.DISTRICT];
		}

		return style;
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
	        dashArray: '',
	        fillOpacity: 0.4
	    });

	    if (!L.Browser.ie && !L.Browser.opera && !L.Browser.edge) {
	        layer.bringToFront();
	    }
	}
	function highlightState(e) {
		highlightFeature(e);
		//  Update Info control with highlighted state's property
		maplet.stateInfoCtrl.update(e.target.feature.properties);
	}
	function highlightDistrict(e) {
		highlightFeature(e);
		//  Update Info control with highlighted districts's property
		maplet.disInfoCtrl.update(e.target.feature.properties);
	}
	function highlightPrecinct(e) {
		highlightFeature(e);
		//  Update Info control with highlighted precincts's property
		maplet.precInfoCtrl.update(e.target.feature.properties);
	}

	// Resets state to original style
	function resetState(e) {
		maplet.geojsonLayer.resetStyle(e.target);
		maplet.stateInfoCtrl.update();
	}
	function resetDistrict(e) {
		maplet.districtLayer.resetStyle(e.target);
		maplet.disInfoCtrl.update();
	}
	function resetPrecinct(e) {
		maplet.precinctLayer.resetStyle(e.target);
		maplet.precInfoCtrl.update();
	}

	// Zooms map to fit state
	maplet.zoomToState = function(e) {
		var layer = e.target;
	    maplet.uiInfo.$rootScope.globalData.selectedState = layer.feature.properties.id;
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
	    	hidePrecinctPanel();
	    	hideDistrictPanel();
			maplet.stateCallback(layer.feature.properties.id, maplet);
		}
	};

	// Maps listener functions to various layer
	function onEachState(feature, layer) {
	    layer.on({
	        mouseover: highlightState,
	        mouseout: resetState,
	        click: maplet.zoomToState
	    });
	}

	function onEachDistrict(feature, layer) {
		layer.on({
			mouseover: highlightDistrict,
			mouseout: resetDistrict
		});
	}

	function onEachPrecinct(feature, layer) {
		layer.on({
			mouseover: highlightPrecinct,
			mouseout: resetPrecinct
		});
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
		maplet.map.on('overlayadd', onOverlayAdd);
		maplet.map.on('overlayremove', onOverlayRemove);

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

	function onOverlayAdd(e) {
		switch(e.name) {
			case "States":
				showStatePanel();
				break;
			case "Current Congressional Districts":
				showDistrictPanel();
				break;
			case "Precincts":
				showPrecinctPanel();
				break;
		}
	}
	function onOverlayRemove(e) {
		switch(e.name) {
			case "States":
				hideStatePanel();
				break;
			case "Current Congressional Districts":
				hideDistrictPanel();
				break;
			case "Precincts":
				hidePrecinctPanel();
				break;
		}
	}

	/**
	 * State Info Panel
	 */
	function infoPanelSetup() {
		statePanelSetup();
		districtPanelSetup();
		precinctPanelSetup();
	}

	function statePanelSetup() {
		maplet.stateInfoCtrl = L.control();
		maplet.stateInfoCtrl.setPosition('bottomleft');

		maplet.stateInfoCtrl.onAdd = function (map) {
			this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
			this.update();
			return this._div;
		};

		// Update info control based on feature properties
		maplet.stateInfoCtrl.update = function (props) {
			this._div.innerHTML = '<h4>State Metrics</h4>' +  (props ?
				'<i>' + props.NAME + '</i><br />' + props.CENSUSAREA + ' people / mi<sup>2</sup>' +
				'<br />' + props.STATE + ' districts'
				: 'Hover over a state');
			this._div.innerHTML += '<br>Zoom: ' + maplet.map.getZoom();
		};

		//maplet.stateInfoCtrl.addTo(maplet.map);
		showStatePanel();
	}
	function showStatePanel() {
		maplet.map.addControl(maplet.stateInfoCtrl);
	}
	function hideStatePanel() {
		maplet.map.removeControl(maplet.stateInfoCtrl);
	}

	function districtPanelSetup() {
		maplet.disInfoCtrl = L.control();
		maplet.disInfoCtrl.setPosition('bottomleft');

		maplet.disInfoCtrl.onAdd = function (map) {
			this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
			this.update();
			return this._div;
		};

		// Update info control based on feature properties
		maplet.disInfoCtrl.update = function (props) {
			this._div.innerHTML = '<h4>District Metrics</h4>' +  (props ?
				'District Number: ' + props.DISTRICT
				: 'Hover over a district');
		};

		//maplet.disInfoCtrl.addTo(maplet.map);
	}
	function showDistrictPanel() {
		maplet.map.addControl(maplet.disInfoCtrl);
	}
	function hideDistrictPanel() {
		maplet.map.removeControl(maplet.disInfoCtrl);
	}

	function precinctPanelSetup() {
		maplet.precInfoCtrl = L.control();
		maplet.precInfoCtrl.setPosition('bottomleft');

		maplet.precInfoCtrl.onAdd = function (map) {
			this._div = L.DomUtil.create('div', 'info'); // create a div with a class "info"
			this.update();
			return this._div;
		};

		// Update info control based on feature properties
		maplet.precInfoCtrl.update = function (props) {
			this._div.innerHTML = '<h4>Precinct Metrics</h4>' +  (props ?
				'<i>' + props.name + '</i><br />' +
				'Population: ' + formatNumber(props.population) + '<br />' +
				'Total Primary Votes: ' + formatNumber(props.votes) + '<br />' +
				'Democrat votes: ' + formatNumber(props.numDemo) + '<br />' +
				'Republican votes: ' + formatNumber(props.numRep) + '<br /><br />' +
				'<h6>Demographic Distribution</h6>' +
				'African American: ' + formatPercent(props.africanAmerican) + '<br />' +
				'Asian/Pacific Islander: ' + formatPercent(props.asian) + '<br />' +
				'Hispanic/Latino: ' + formatPercent(props.latinAmerican) + '<br />'
				: 'Hover over a precinct');
		};

		//maplet.precInfoCtrl.addTo(maplet.map);
	}
	function showPrecinctPanel() {
		maplet.map.addControl(maplet.precInfoCtrl);
	}
	function hidePrecinctPanel() {
		maplet.map.removeControl(maplet.precInfoCtrl);
	}

	// Info Panel Helper functions
	function formatNumber(num) {
		var numRepr = (num).toString();
		var commaGap = 3;
		var result = "";
		var gapCount = commaGap;
		for (var i = numRepr.length - 1; i >= 0; i--) {
			if (gapCount == 0) {
				result = numRepr[i] + ',' + result;
				gapCount = commaGap;
			} else {
				result = numRepr[i] + result;
				gapCount--;
			}
		}
		return result;
	}

	function formatPercent(num) {
		var numStr = (num * 100).toString();
		var endIndex = (numStr.length > 4) ? 4 : numStr.length;
		var result = numStr.substring(0, endIndex) + '%';
		return result;
	}

	/**
	 * Spatial Methods
	 */
	function updateClusterProps(prop1, prop2, remove) {
		var propLabels = ['population', 'votes', 'numDemo', 'numRep', 'white',
						  'africanAmerican', 'asian', 'latinAmerican', 'other'];
		var coeff = (remove) ? -1 : 1;
		for (var i = 0; i < propLabels.length; i++) {
			var label = propLabels[i];
			prop1[label] += coeff * prop2[label];
		}
		return prop1;
	}

	maplet.moveCluster = function(precinctId, oldClusterId, newClusterId) {
		var precinct = maplet.precinctLayer.getLayer(maplet.leaf_precincts[precinctId]);
		var precGeo = precinct.toGeoJSON();
		var oldCluster = maplet.clusterLayer.getLayer(maplet.leaf_clusters[oldClusterId]);
		var oldClusGeo = oldCluster.toGeoJSON();
		maplet.clusterLayer.removeLayer(oldCluster);
		var oldFeature = turf.difference(oldClusGeo, precGeo);
		if (oldFeature) {
			oldFeature.properties = updateClusterProps(oldFeature.properties,
													   precGeo.properties, true);
			oldClusGeo = oldFeature;
			// Convert to layer
			oldCluster = L.GeoJSON(oldClusGeo, {
				style: precinctStyle,
				onEachFeature: onEachPrecinct
			});
			maplet.clusterLayer.addLayer(oldCluster);
		}

		var newCluster = maplet.clusterLayer.getLayer(maplet.leaf_clusters[newClusterId]);
		var newClusGeo = newCluster.toGeoJSON();
		maplet.clusterLayer.removeLayer(newCluster);
		newClusGeo = turf.union(newClusGeo, precGeo);
		newClusGeo.properties = updateClusterProps(newClusGeo.properties,
														   precGeo.properties, false);
		newCluster = L.GeoJSON(newClusGeo, {
			style: precinctStyle,
			onEachFeature: onEachPrecinct
		});
		maplet.clusterLayer.addLayer(newCluster);
	}
}