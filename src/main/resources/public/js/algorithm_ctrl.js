'use strict';

function AlgIntermissionPanel(AlgorithmService, $scope, $rootScope, $element, $attrs) {
    var ctrl = this;
    $scope.AlgorithmService = AlgorithmService;
    $scope.username = $rootScope.globalData.user.username;
}

angular.module('DistrictApp').component('algIntermission', {
    scope: false,
    templateUrl: 'templates/algInter.tmpl.html',
    controller: AlgIntermissionPanel
});

function AlgSummaryPanel(AlgorithmService, $scope, $rootScope, $element, $attrs) {
    var ctrl = this;
    $scope.AlgorithmService = AlgorithmService;
    $scope.username = $rootScope.globalData.user.username;
    var mainScope = angular.element("#appShell").scope();
    $scope.formatNumber = mainScope.usMap.formatNumber;
    $scope.formatPercent = mainScope.usMap.formatPercent;
    if (!ctrl.summaryInfo) {
        ctrl.summaryInfo = mainScope.summaryObj;
    }
    ctrl.scoreBefore = ctrl.summaryInfo.scoreBefore;
    ctrl.scoreAfter = ctrl.summaryInfo.scoreAfter;
    ctrl.stateStat = ctrl.summaryInfo.stateStats;
    ctrl.actualStat = ctrl.summaryInfo.actualStats;
    ctrl.districtStat = ctrl.summaryInfo.districtStat;
    //ctrl.majMinIds = ctrl.summaryInfo.majMinIds;
}

angular.module('DistrictApp').component('algSummary', {
    scope: false,
    templateUrl: 'templates/summary.tmpl.html',
    controller: AlgSummaryPanel,
    bindings: {
        summaryInfo: '='
    }
});