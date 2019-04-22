function GuiGroupController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('guiGroup', {
  templateUrl: 'templates/guiGroup.tmpl.html',
  controller: GuiGroupController,
  bindings: {
  	group: '='
  }
});

function TextBtnController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('textBtn', {
  templateUrl: 'templates/textBtn.tmpl.html',
  controller: TextBtnController,
  bindings: {
  	button: '='
  }
});

function IconBtnController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('iconBtn', {
  templateUrl: 'templates/iconBtn.tmpl.html',
  controller: IconBtnController,
  bindings: {
  	button: '='
  }
});

function MenuController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('menu', {
  templateUrl: 'templates/menu.tmpl.html',
  controller: MenuController,
  bindings: {
  	menu: '='
  }
});

function NumberInputController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('numInput', {
  templateUrl: 'templates/numInput.tmpl.html',
  controller: NumberInputController,
  bindings: {
  	input: '='
  }
});

function SliderMeasureController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('slideMeasure', {
  templateUrl: 'templates/slideMeasure.tmpl.html',
  controller: SliderMeasureController,
  bindings: {
  	slider: '='
  }
});

function RangeMeasureController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('rangeMeasure', {
  templateUrl: 'templates/rangeMeasure.tmpl.html',
  controller: rangeMeasureController,
  bindings: {
  	range: '='
  }
});

function SelectController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('selectDisplay', {
  templateUrl: 'templates/select.tmpl.html',
  controller: SelectController,
  bindings: {
  	select: '='
  }
});

function AccordionController($scope, $element, $attrs) {
	var ctrl = this;
}

angular.module('districtApp').component('accordionTab', {
  templateUrl: 'templates/accordionTab.tmpl.html',
  controller: AccordionController,
  bindings: {
  	accordion: '='
  }
});