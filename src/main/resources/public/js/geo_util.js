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
        service.loadStateDistricts = function(stateId) {
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
        service.loadStatePrecincts = function(stateId) {
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