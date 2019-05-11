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

        return service;
    });