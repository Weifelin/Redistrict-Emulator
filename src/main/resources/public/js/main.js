'use strict';

var app = angular.module('DistrictApp', ['prop', 'ngMaterial', 'ngMessages', 'ngAnimate']);

app.run(function($rootScope, $mdDialog, $mdToast) {
	$rootScope.funcMap = {
    		"changeTabState": changeTabState,
    		"updateMajMinSliders": updateMajMinSliders,
            "login": function() {
                var app = 
                $mdDialog.show({
                    locals: {
                        user: app.username,
                        pass: "",
                        act: "login"
                    },
                    controller: LoginController,
                    templateUrl: 'login.tmpl.html',
                    parent: angular.element(document.body),
                    targetEvent: ev,
                    clickOutsideToClose:false
                })
                .then(function(answer) {
                    if (answer.action == "login") {
                        app.username = answer.username;
                        console.log(answer.username + " has logged in.");
                        $mdToast.showSimple("Welcome back " + app.username + "!");
                    }
                    else if (answer.action == "signup") {
                        app.username = answer.username;
                        console.log(answer.username + " is now registered.");
                        $mdToast.showSimple("Welcome, " + app.username + "!");
                    }
                    else {
                        console.log("There is a disturbance in the login mechanism...");
                    }
                }, function() {
                    console.log('User decided to continue as a guest.');
                });
            },
            "logout": logout
    };
});

app.controller('AppCtrl', function(GenProp, $scope, $rootScope) {
    	var ctrl = this;
    	// Load General Component Properties 
    	$scope.componentProp = GenProp.query();
        // Query backend for state list (with initial data)
 
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

