'use strict';
angular.module('AlgoUtil')
    .factory('AlgorithmService', function($rootScope, $mdToast) {
        var service = this;

        service.startSingleRun = function() {
            if ($rootScope.globalData.selectedState !== "") {
                $rootScope.globalData.programState = $rootScope.programStates.RUNNING;
                var url = "single-run";
                var data = angular.element("#appShell").scope().content["singleRun"].packData();
                console.log(data);
                // $http.post(url);
            } else {
                $mdToast.showSimple("State not selected.");
            }
        };

        return service;
    });