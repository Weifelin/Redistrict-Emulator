'use strict';
angular.module('GeoUtil')
    .factory('GeoDataService', function($resource) {
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
        service.loadStateDistrict = function(stateID) {
            var url = "originalCongressionalDistricts.json";
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
        service.loadStatePrecinct = function(stateID) {
            var url = "GeoNJPrecincts.json";
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