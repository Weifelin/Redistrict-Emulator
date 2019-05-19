'use strict';

function AlgIntermissionPanel(AlgorithmService, $scope, $rootScope, $element, $attrs) {
    var ctrl = this;
}

angular.module('DistrictApp').component('algIntermission', {
    scope: false,
    templateUrl: 'templates/algInter.tmpl.html',
    controller: AlgIntermissionPanel
});

function AlgSummaryPanel(AlgorithmService, $scope, $rootScope, $element, $attrs) {
    var ctrl = this;
    ctrl.scoreBefore = ctrl.summaryInfo.scoreBefore;
    ctrl.scoreAfter = ctrl.summaryInfo.scoreAfter;
    ctrl.stateStat = ctrl.summaryInfo.stateStat;
    ctrl.actualStat = ctrl.summaryInfo.actualStat;
    ctrl.districtStat = ctrl.summaryInfo.districtStat;
    ctrl.majMinIds = ctrl.summaryInfo.majMinIds;
}

angular.module('DistrictApp').component('algSummary', {
    scope: false,
    templateUrl: 'templates/summary.tmpl.html',
    controller: AlgSummaryPanel,
    bindings: {
        summaryInfo: '>'
    }
});