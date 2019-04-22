function MeasureSlider(value, id, min, max, step, tooltip) {
  this.value = value;
  this.id = id;
  this.min = min;
  this.max = max;
  this.step = step;
  this.tooltip = tooltip;
  this.category = "slider";
}

function MeasureNumInput() {}

(function() {
  'use strict';

  angular.module('MapApp', ['ngMaterial', 'ngMessages', 'ngAnimate'])
    .controller('AppCtrl', function($scope, $mdDialog, $mdToast) {
      var app = this;
      app.test = "(demo.username != '') && (uiInfo.selectedState != '') && (demo.mode == 'batch')";

      /* Login Info */
      app.username = "";
      app.modes = ["single", "batch"];
      app.mode = "single";

      /* Call leaflet function (potentially create a Map obj instead) */
      $scope.uiInfo = {
        selectedState: ""
      };
      app.usMap = new Map($scope.uiInfo);
      app.usMap.mapSetup();
      $scope.$watch('uiInfo', function (newValue, oldValue, scope) {
        var select = document.getElementsByClassName("stateSelect")[0];
        select.focus();
      });

      app.isOpen = false;
      app.hover = false;

      app.selectedMode = 'md-scale';

      app.selectedDirection = 'left';

      
      // Dialog
      $scope.showLoginDialog = function(ev) {
        $mdDialog.show({
          locals: {
            user: app.username,
            pass: "",
            act: "login"
          },
          controller: LoginController,
          templateUrl: 'login.tmpl.html',
          parent: angular.element(document.body),
          targetEvent: ev,
          clickOutsideToClose:false
        })
        .then(function(answer) {
          if (answer.action == "login") {
            app.username = answer.username;
            console.log(answer.username + " has logged in.");
            $mdToast.showSimple("Welcome back " + app.username + "!");
          }
          else if (answer.action == "signup") {
            app.username = answer.username;
            console.log(answer.username + " is now registered.");
            $mdToast.showSimple("Welcome, " + app.username + "!");
          }
          else {
            console.log("There is a disturbance in the login mechanism...");
          }
        }, function() {
          console.log('You decided to continue as a guest.');
        });
      };

      /* Defining menu item info */
      app.items = [
        { name: "Login", class:"", icon: "img/icons/baseline-person_outline-24px.svg", direction: "bottom", show: true, click: $scope.showLoginDialog },
        { name: "Log out", class:"", icon: "img/icons/baseline-exit_to_app-24px.svg", direction: "bottom", show: false, click: "" },
        { name: "Batch Run", class:"", icon: "img/icons/baseline-add_photo_alternate-24px.svg", direction: "bottom", show: false, click: "" },
        { name: "Single Run", class:" currMode", icon: "img/icons/baseline-photo-24px.svg", direction: "bottom", show: true, click: "" }
      ];
      $scope.clickEvent = function($event, item) {
        if (item.name == "Login") {
          item.click($event);
          item.show = !item.show;
          app.items[1].show = !item.show;
          app.items[2].show = !item.show;
        }
        else if (item.name == "Log out") {
          item.show = !item.show;
          app.items[0].show = !item.show;
          app.username = "";
          app.items[2].show = !item.show;
          $mdToast.showSimple("Log out successful.");
        }
        else if (item.name == "Single Run") {
          app.mode = "single";
          app.items[2].class = "";
          app.items[3].class = " currMode";
        }
        else if (item.name == "Batch Run") {
          app.mode = "batch";
          app.items[2].class = " currMode";
          app.items[3].class = "";
        }
      };

      /* Handling application menu */
      app.buttonIcons = {
      	prev: { name: "Previous State", icon: "img/icons/baseline-navigate_before-24px.svg"},
      	next: { name: "Next State", icon: "img/icons/baseline-navigate_next-24px.svg"}
      };
      app.states=app.usMap.leaf_states;
      /* Algorithm Control Menu */
      $scope.testing = "algData.general.start";
      $scope.algData = {
        general: {
          numDistricts:null,
          start:null,
          end:null
        },
        compact: {
          "Polsby-popper": new MeasureSlider(1, "polpop-slider", 0, 1, 0.01, "tooltip"),
          "Schwartzberg": new MeasureSlider(1, "schwartz-slider", 0, 1, 0.01, "tooltip"),
          "Convex Hull": new MeasureSlider(1, "convhull-slider", 0, 1, 0.01, "tooltip"),
          "Reock": new MeasureSlider(1, "reock-slider", 0, 1, 0.01, "tooltip")
        },
        contig: {
          "Weight": new MeasureSlider(1, "contig-slider", 0, 1, 0.01, "tooltip")
        },
        popEq: {
          "Deviation Threshold": new MeasureSlider(1, "devThresh-slider", 0, 1, 0.01, "tooltip")
        },
        parFai: {
          "Weight": new MeasureSlider(1, "parFai-slider", 0, 1, 0.01, "tooltip")
        },
        majMinDis: {
          "Minimum %": new MeasureSlider(0, "parFai-slider", 0, 100, 1, "tooltip"),
          "Maximum %": new MeasureSlider(100, "parFai-slider", 0, 100, 1, "tooltip")
        }
      };

      // Creating the Accordion tabs
      app.actions = {
        max: { name: "Maximize", icon: "img/icons/baseline-add-24px.svg"},
        min: { name: "Minimize", icon: "img/icons/baseline-remove-24px.svg"}
      };
      $scope.accordionTabs = {
        compact: {title: "Compactness", action: "min", open:true, order:1},
        contig: {title: "Contiguity", action: "max", open:false, order:3},
        popEq: {title: "Population Equality", action: "max", open:false, order:5},
        parFai: {title: "Partisan Fairness", action: "max", open:false, order:7},
        majMinDis: {title: "Majority-Minority Districts", action: "max", open:false, order:9},
      };
      $scope.changeTabState = function(tabId) {
        var act = $scope.accordionTabs[tabId].action;
        $scope.accordionTabs[tabId].action = (act === "max") ? "min" : "max";
        $scope.accordionTabs[tabId].open = (act === "max") ? true : false;
        if ($scope.accordionTabs[tabId].open) {
          for(var key in $scope.accordionTabs) {
            var currTab = $scope.accordionTabs[key];
            if (key != tabId && currTab.open) {
              $scope.accordionTabs[key].action = "max";
              $scope.accordionTabs[key].open = false;
            }
          }
        }
      };


      $scope.menuToggle = {};

      function LoginController($scope, $mdDialog, user, pass, act) {
        $scope.username = user;
        $scope.password = pass;
        $scope.act = act;
        $scope.hide = function() {
          $mdDialog.hide();
        };

        $scope.cancel = function() {
          $mdDialog.cancel();
        };

        $scope.answer = function(answer) {
          var response = {
            username: $scope.username,
            password: $scope.password,
            action: $scope.act
          }
          $mdDialog.hide(response);
        };
      }
    }); 
})();