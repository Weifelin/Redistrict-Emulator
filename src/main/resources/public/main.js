'use strict';

var app = angular.module('DistrictApp', ['prop', 'ngMaterial', 'ngMessages', 'ngAnimate']);

app.run(function($rootScope) {
	$rootScope.funcMap = {
    		"changeTabState": changeTabState,
    		"updateMajMinSliders": updateMajMinSliders
    };
});

app.controller('AppCtrl', function(GenProp, $scope, $rootScope) {
    	var ctrl = this;
    	// Load General Component Properties 
    	$scope.componentProp = GenProp.query();
 
    	$scope.content = {};
 
    	$scope.userInfo = {
    		username : "",
    		level : "guest"
    	};

    	// Map Setup 
    	$scope.uiInfo = {
        	selectedState: ""
	    };
	    ctrl.usMap = new Map($scope.uiInfo);
	    ctrl.usMap.mapSetup();
});

