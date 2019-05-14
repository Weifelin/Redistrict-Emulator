function GuiGroupController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('guiGroup', {
  scope: false,
  templateUrl: 'templates/guiGroup.tmpl.html',
  controller: GuiGroupController,
  bindings: {
  	group: '='
  }
});

function TextBtnController(AlgorithmService, $scope, $element, $attrs) {
	var ctrl = this;
	$scope.AlgorithmService = AlgorithmService;
}

angular.module('DistrictApp').component('textButton', {
  scope: false,
  templateUrl: 'templates/textBtn.tmpl.html',
  controller: TextBtnController,
  bindings: {
  	button: '='
  }
});

function IconBtnController(AccountActionService, AccountDialogService, GeneralUtilService,
                           $scope, $element, $attrs, $rootScope) {
	var ctrl = this;
	$scope.AccountDialogService = AccountDialogService;
    $scope.AccountActionService = AccountActionService;
    $scope.GeneralUtilService = GeneralUtilService;
    $scope.$rootScope = $rootScope;
}

angular.module('DistrictApp').component('iconButton', {
  scope: false,
  templateUrl: 'templates/iconBtn.tmpl.html',
  controller: IconBtnController,
  bindings: {
  	button: '='
  }
});

function MenuController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('menu', {
  scope: false,
  templateUrl: 'templates/menu.tmpl.html',
  controller: MenuController,
  bindings: {
  	menu: '='
  }
});

function NumberInputController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('numberInput', {
  scope: false,
  templateUrl: 'templates/numInput.tmpl.html',
  controller: NumberInputController,
  bindings: {
  	input: '='
  }
});

function SliderMeasureController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('slideMeasure', {
  scope: false,
  templateUrl: 'templates/slideMeasure.tmpl.html',
  controller: SliderMeasureController,
  bindings: {
  	slider: '='
  }
});

function RangeMeasureController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('rangeMeasure', {
  scope: false,
  templateUrl: 'templates/rangeMeasure.tmpl.html',
  controller: RangeMeasureController,
  bindings: {
  	range: '='
  }
});

function SelectController(GeneralUtilService, $scope, $element, $attrs) {
	var ctrl = this;
	$scope.GeneralUtilService = GeneralUtilService;
}

angular.module('DistrictApp').component('selectDisplay', {
  scope: false,
  templateUrl: 'templates/select.tmpl.html',
  controller: SelectController,
  bindings: {
  	select: '='
  }
});

function AccordionController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('DistrictApp').component('accordionTab', {
  scope: false,
  templateUrl: 'templates/accordionTab.tmpl.html',
  controller: AccordionController,
  bindings: {
  	accordion: '='
  }
});