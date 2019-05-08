'use strict';

function AppMenuController(AppMenuProp, $scope, $rootScope, $element, $attrs) {
	var ctrl = this;
  ctrl.sectionId = "appMenu";

  var appMenuStruct = AppMenuProp.query();
  appMenuStruct.$promise.then(function() {
    console.log(ctrl.content);
    ctrl.content[ctrl.sectionId] = parseGui(appMenuStruct, ctrl.componentProp, []);
  });
}

angular.module('DistrictApp').component('appMenu', {
  scope: false,
  templateUrl: 'templates/pageSection.tmpl.html',
  controller: AppMenuController,
  bindings: {
    content: "=",
  	componentProp: '='
  }
});