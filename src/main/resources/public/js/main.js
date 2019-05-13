'use strict';
angular.module('GeoUtil', []);
angular.module('GuiUtil', []);
angular.module('AccountAction', ['ngMaterial', 'ngMessages']);

var app = angular.module('DistrictApp',
    ['prop', 'AccountAction', 'GuiUtil', 'GeoUtil', 'ngRoute', 'ngCookies',
            'ngMaterial', 'ngMessages', 'ngAnimate']);

// Configure routes
app.config(['$locationProvider', '$routeProvider',
    function ($locationProvider, $routeProvider) {
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
        $rootScope.programStates = { FREE: 0, RUNNING: 1 };
        $rootScope.globalData = $cookies.get('globalData');
        if (!$rootScope.globalData) {
            $rootScope.globalData = {
                user: {
                    username: "",
                    password: "",
                    userType: $rootScope.userTypes.GUEST
                },
                mode: "singleRun",
                selectedState: "",
                programState: $rootScope.programStates.FREE
            };
        } else if ($rootScope.globalData.user) {
            $http.defaults.headers.common['XSRF-TOKEN'] = 'Basic ' +
                                            $rootScope.globalData.user.username +
                                            $rootScope.globalData.user.password;
        }
}]);

app.controller('AppCtrl', function(GenProp, GeoDataService, $scope, $rootScope, $mdToast) {
    	var ctrl = this;
    	$scope.$rootScope = $rootScope;
    	// Load General Component Properties 
    	GenProp.query().$promise
            .then(function(componentProp) {
                $scope.componentProp = componentProp;
            });
 
    	$scope.content = {};

    	// Map Setup
        // Query backend for state list (with initial data)
        $rootScope.statesGeoJSON = GeoDataService.loadStates().query().$promise
            .then(function(statesGeoJSON) {
                $rootScope.statesGeoJSON = statesGeoJSON;
                $scope.uiInfo = {
                    selectedState: "",
                    states: $rootScope.statesGeoJSON
                };
                $scope.usMap = new Map($scope.uiInfo);
                $scope.usMap.GeoDataService = GeoDataService;
                $scope.usMap.$mdToast = $mdToast;
                $scope.usMap.stateCallback = function(stateId, map) {
                    // Load precincts and districts, ensuring both load properly
                    var precinctFetch = map.GeoDataService.loadStatePrecincts(stateId).query().$promise;
                    map.GeoDataService.loadStateDistricts(stateId).query().$promise
                        .then(function(districtGeoJSON) {
                            precinctFetch.then(function(precinctGeoJSON) {
                                map.initDistricts(districtGeoJSON);
                                map.initPrecincts(precinctGeoJSON);
                            }, function() {
                                map.$mdToast.showSimple("Could not load Precincts for " +
                                    map.uiInfo.selectedState + ".");
                                // Insert deselect state
                            });
                        }, function() {
                            map.$mdToast.showSimple("Could not load Districts for " +
                                                map.uiInfo.selectedState + ".");
                            // Insert deselect state
                        });
                };
                $scope.usMap.mapSetup();
            });
});

