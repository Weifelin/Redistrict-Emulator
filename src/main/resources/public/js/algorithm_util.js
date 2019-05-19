'use strict';
angular.module('AlgoUtil')
    .factory('AlgorithmService', function($rootScope, $mdToast, $http) {
        var service = this;
        service.formatData = function(data) {
            var result = {};
            result["stateE"] = $rootScope.globalData.selectedState;
            result["numDistricts"] = data.mainControls.numDistrictInput;
            result["compactnessWeight"] = {
                "polsPopSlider": data.measureTabs.compactnessTab.polsPopSlider,
                "schwartzSlider": data.measureTabs.compactnessTab.schwartzSlider,
                "convexHullSlider": data.measureTabs.compactnessTab.convexHullSlider,
                "reockSlider": data.measureTabs.compactnessTab.reockSlider
            };
            result["partyFairSlider"] = data.measureTabs.partFairTab.partFairSlider;
            result["efficiencyGapSlider"] = data.measureTabs.partFairTab.efficiencyGapSlider;
            result["populationEquality"] = data.measureTabs.popEqTab.popEqSlider;
            result["numMajorityMinorityDistricts"] = data.measureTabs.majMinTab.numMajMin;
            var demoList = ["africanAmericanRange", "asianRange", "latinAmericanRange"];
            angular.forEach(demoList, function(demo) {
                var rangeGroup = data.measureTabs.majMinTab.minSettingsGroup;
                if (rangeGroup[demo]) {
                    result[demo.replace("Range", "")] =	{
                        "low": rangeGroup[demo].low,
                        "high": rangeGroup[demo].high
                    };
                } else {
                    result[demo.replace("Range", "")] = null;
                }
            }, result);

            return result;
        };

        service.startSingleRun = function() {
            if ($rootScope.globalData.selectedState !== "") {
                $rootScope.globalData.programState = $rootScope.programStates.RUNNING;
            } else {
                $mdToast.showSimple("State not selected.");
                return null;
            }
        };

        service.makeSingleRunRequest = function() {
            var url = "single-run";
            var data = angular.element("#appShell").scope().content["singleRun"].packData();
            data = service.formatData(data);
            return $http.post(url, data);
        };

        service.startSimAnneal = function() {
            var url = "start-simulatedAnnealing";
            $http.post(url, {}).then(function(success) {
                // Make interval that requests 'getmoves' until flag is received
            });
        };

        return service;
    });