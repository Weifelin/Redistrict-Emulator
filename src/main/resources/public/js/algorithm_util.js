'use strict';
angular.module('AlgoUtil')
    .factory('AlgorithmService', function($rootScope, $mdToast) {
        var service = this;

        service.startSingleRun = function() {
            if ($rootScope.globalData.selectedState !== "") {
                $rootScope.globalData.programState = $rootScope.programStates.RUNNING;
            } else {
                $mdToast.showSimple("State not selected.");
            }
        };

        return service;
    });