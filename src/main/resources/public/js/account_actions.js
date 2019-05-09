'use strict';

angular.module('AccountAction')
    .factory('AccountActionService', ['$http', '$cookies', '$rootScope'],
        function($http, $cookies, $rootScope) {
            var service = {
                authUser: function(username, password, url, successCall, errorCall) {
                    // Salt password here
                    $http.post(url,
                        {
                            username: username,
                            password: password
                        })
                    .then(function(response) {
                        successCall(response);
                    }, function(response) {
                        errorCall(response);
                    });
                },
                logout: function() {
                    
                }
            };
        });

function login() {
    var appCtrl = angular.element("#appShell");
    var userInfo = appCtrl.scope().userInfo;
    var dialogService = appCtrl.injector().get('$mdDialog');
    var toastService = appCtrl.injector().get('$mdToast');
    var windowWrapper = appCtrl.injector().get('$window');
    windowWrapper.history.pushState({}, "Log In", "login");
	dialogService.show({
        locals: {
            user: userInfo.username,
            pass: "",
            act: "login"
        },
        controller: LoginController,
        templateUrl: 'templates/login.tmpl.html',
        parent: angular.element(document.body),
        clickOutsideToClose:false
    })
    .then(function(answer) {
        if (answer.action == "login") {
            userInfo.username = answer.username;
            console.log(answer.username + " has logged in.");
            toastService.showSimple("Welcome back " + answer.username + "!");
        }
        else if (answer.action == "signup") {
            userInfo.username = answer.username;
            console.log(answer.username + " is now registered.");
            toastService.showSimple("Welcome, " + answer.username + "!");
        }
        else {
            console.log("There is a disturbance in the login mechanism...");
        }
    }, function() {
        console.log('User decided to continue as a guest.');
    });
}

function loginRequest(credentials) {

}

function signupRequest(credentials) {

}

function logout() {
	
}

function logoutRequest() {
	
}

function startSingleRun() {
	var mainCtrlScope = angular.element("#appShell").scope();
	var data = mainCtrlScope.content["singleRun"].packData();
	// Make algorithm start request
}

function LoginController($scope, $mdDialog, $location, user, pass, act) {
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

    $scope.switchAction = function(newAction) {
        $scope.act = newAction;
        var title = ($scope.act == 'login') ? 'Log In' : 'Sign Up';
        $location.path($scope.act);
        //$window.history.replaceState({}, title, $scope.act);
    }
}