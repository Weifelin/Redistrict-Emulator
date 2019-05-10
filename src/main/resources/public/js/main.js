'use strict';
angular.module('GuiUtil', []);
angular.module('AccountAction', []);

var app = angular.module('DistrictApp',
    ['prop', 'AccountAction', 'GuiUtil', 'ngRoute', 'ngCookies',
            'ngMaterial', 'ngMessages', 'ngAnimate']);

// Configure routes
app.config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('');
    $locationProvider.html5Mode({
        enabled: true,
        requireBase: false
    });
    $routeProvider
        .when('/login', {
            controller: 'LoginController',
            template: ' '
        })
        .when('/register', {
            controller: 'LoginController',
            template: ' '
        });
}]);

// Set up global variables (or pull from cookieStore)
app.run(['$rootScope', '$cookies', '$http',
    function($rootScope, $cookies, $http) {
        $rootScope.userTypes = { REGULAR: 0, ADMIN: 1, GUEST: 2 };
        $rootScope.globalData = $cookies.get('globalData');
        if (!$rootScope.globalData) {
            $rootScope.globalData = {
                user: {
                    username: "",
                    password: "",
                    userType: $rootScope.userTypes.GUEST
                }
            };
        } else if ($rootScope.globalData.user) {
            $http.defaults.headers.common['Authorization'] = 'Basic ' +
                                            $rootScope.globalData.user.username +
                                            $rootScope.globalData.user.password;
        }
}]);

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

