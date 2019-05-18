'use strict';
angular.module('GeoUtil', []);
angular.module('GuiUtil', []);
angular.module('AccountAction', ['ngMaterial', 'ngMessages']);
angular.module('AlgoUtil', []);

var app = angular.module('DistrictApp',
    ['prop', 'AccountAction', 'GuiUtil', 'GeoUtil', 'AlgoUtil',
            'ngRoute', 'ngCookies', 'ngMaterial', 'ngMessages', 'ngAnimate']);

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
        $rootScope.demographics = { AFRICAN_AMERICAN: 0, ASIAN: 1, HISPANIC: 2, WHITE: 3 };
        $rootScope.stateCode = { 0: "NJ", 1: "VA", 2: "WI" };
        $rootScope.loading = false;
        $rootScope.globalData = $cookies.get('globalData');
        if (!$rootScope.globalData || $rootScope.globalData == "[object Object]") {
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

app.controller('AppCtrl', function(GenProp, GeoDataService, AlgorithmService , $scope, $rootScope, $mdToast) {
    	var ctrl = this;
    	$scope.$rootScope = $rootScope;
    	// Load General Component Properties 
    	GenProp.query().$promise
            .then(function(componentProp) {
                $scope.componentProp = componentProp;
            });
 
    	$scope.content = {};

    	// Map Setup
        $scope.usMap;
        // Query backend for state list (with initial data)
        $rootScope.statesGeoJSON = GeoDataService.loadStates().query().$promise
            .then(function(statesGeoJSON) {
                $rootScope.statesGeoJSON = statesGeoJSON;
                $scope.uiInfo = {
                    "$rootScope": $rootScope,
                    states: $rootScope.statesGeoJSON
                };
                $scope.usMap = new Map($scope.uiInfo);
                $scope.usMap.GeoDataService = GeoDataService;
                $scope.usMap.$mdToast = $mdToast;
                $scope.usMap.stateCallback = function(stateId, map) {
                    // Load precincts and districts, ensuring both load properly
                    $rootScope.loading = true;
                    var precinctFetch = map.GeoDataService.loadStatePrecincts(stateId).query().$promise;
                    map.GeoDataService.loadStateDistricts(stateId).query().$promise
                        .then(function(districtGeoJSON) {
                            precinctFetch.then(function(precinctGeoJSON) {
                                map.initDistricts(districtGeoJSON);
                                map.initPrecincts(precinctGeoJSON);
                                $rootScope.loading = false;
                            }, function() {
                                map.$mdToast.showSimple("Could not load Precincts for " +
                                    map.uiInfo.selectedState + ".");
                                $rootScope.loading = false;
                                // Insert deselect state
                            });
                        }, function() {
                            map.$mdToast.showSimple("Could not load Districts for " +
                                                map.uiInfo.selectedState + ".");
                            $rootScope.loading = false;
                            // Insert deselect state
                        });
                };
                $scope.usMap.mapSetup();
            });
        $rootScope.$watch('globalData.programState', function(newVal, oldVal) {
            if (newVal == $rootScope.programStates.RUNNING &&
                oldVal == $rootScope.programStates.FREE) {
                $scope.usMap.initClusters();
                AlgorithmService.startSingleRun(function(response) {
                    if(response.data !== "") {
                        angular.forEach(response.data.simpleClusterGroups, function(cluster) {
                            var clusterID = cluster.clusterID;
                            var seedPrecinct = cluster.precinctList[0];
                            var newPrecinctList = cluster.precinctList.slice(1);
                            $scope.usMap.addPrecinctSeed(clusterID, seedPrecinct);
                            angular.forEach(newPrecinctList, function(precinctID) {
                                $scope.usMap.moveCluster(precinctID, precinctID, clusterID);
                            });
                        });
                    }
                });
            }
        });
});

