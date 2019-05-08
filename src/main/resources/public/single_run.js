'use strict';

function SingleRunController(SingleRunProp, $scope, $rootScope, $element, $attrs) {
	var ctrl = this;
  ctrl.sectionId = "singleRun";

  var singleRunStruct = SingleRunProp.query();
  singleRunStruct.$promise.then(function() {
    console.log(ctrl.content);
    ctrl.content[ctrl.sectionId] = parseGui(singleRunStruct, ctrl.componentProp, []);
  });
}

angular.module('DistrictApp').component('singleRun', {
  scope: false,
  templateUrl: 'templates/pageSection.tmpl.html',
  controller: SingleRunController,
  bindings: {
    content: "=",
  	componentProp: '='
  }
});