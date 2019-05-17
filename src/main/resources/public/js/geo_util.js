'use strict';
angular.module('GeoUtil')
    .factory('GeoDataService', function($rootScope, $resource) {
        var service = this;
        service.loadStates = function() {
            //var url = "/states";
            var url = "States.json";
            return $resource(url, {}, {
                query: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                },
                get: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                }
            });
        };
        service.loadStateDistricts = function(stateId) {
            var url = $rootScope.stateCode[stateId] + "CongressionalDistricts.json";
            return $resource(url, {}, {
                query: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                },
                get: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                }
            });
        };
        service.loadStatePrecincts = function(stateId) {
            var url = $rootScope.stateCode[stateId] + "GeoPrecincts.json";

            return $resource(url, {}, {
                query: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                },
                get: {
                    method: "GET",
                    params: {},
                    isArray: false,
                    cache: true
                }
            });
        };

        return service;
    });