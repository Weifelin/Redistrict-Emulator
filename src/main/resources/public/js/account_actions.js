'use strict';

angular.module('AccountAction')
    .factory('AccountActionService', ['$http', '$cookies', '$rootScope', '$q',
        function($http, $cookies, $rootScope, $q) {
            var service = this;
            service.updateUserInfo = function(username, password, userType) {
                    $rootScope.globalData.user = {
                        username: username,
                        password: password,
                        userType: userType
                    };

                    $cookies.put('globalData', $rootScope.globalData);

                    $http.defaults.headers.common['XSRF-TOKEN'] = 'Basic ' +
                        $rootScope.globalData.user.username +
                        $rootScope.globalData.user.password;
                };
            service.clearUserInfo = function() {
                    $rootScope.globalData = {
                        user: {
                            username: "",
                            password: "",
                            userType: $rootScope.userTypes.GUEST
                        },
                        mode: "singleRun",
                        selectedState: "",
                        programState: $rootScope.programStates.FREE
                    };
                    $cookies.remove('globalData');

                    $http.defaults.headers.common['XSRF-TOKEN'] = 'Basic ';
                };
            service.authUser = function(username, password, url, successCall, errorCall) {
                    //Salt password here
                    var pwd;
                    var salt;
                    var saltPromise;
                    if (url == "register"){
                        salt = generateSalt();
                        saltPromise = $q(function(resolve, reject) {
                            resolve({ data: { saltString: salt } });
                        });
                    }

                    if (url == "login"){
                        var salturl;
                        salturl = "/"+username+"/salt";
                        saltPromise = $http.get(salturl,{})
                    }

                    saltPromise.then(function(saltResponse) {
                        salt = saltResponse.data.saltString;
                        pwd = password;
                        pwd = pwd.concat(salt);
                        /*Please do hash here*/

                        var data = {
                            username: username,
                            password: pwd,
                            salt: salt,
                            userType: $rootScope.userTypes.REGULAR
                        };
                        console.log(data);
                        $http.post(url, data)
                            .then(function(response) {
                                successCall(response);
                            }, function(response) {
                                errorCall(response);
                            });

                    }, function(saltError) {
                        errorCall(saltError);
                    });
                };
            service.logout = function() {
                    // Add username as data and potentially add callback on success/failure
                    $http.post('logout', {})
                        .then(function(response) {
                            service.clearUserInfo();
                        });
                };

            return service;
        }])

    .factory('AccountDialogService',
        ['$http', '$mdDialog', '$mdToast', '$location',
        function($http, $mdDialog, $mdToast, $location) {
            var service = {
                open: function() {
                    $location.path("login");
                    $mdDialog.show({
                        locals: {
                            user: "",
                            pass: "",
                            act: "login",
                            isLoading: false
                        },
                        controller: LoginController,
                        templateUrl: 'templates/login.tmpl.html',
                        parent: angular.element(document.body),
                        clickOutsideToClose:false
                    })
                        .then(function(answer) {
                            if (answer.action == "login") {
                                console.log(answer.username + " has logged in.");
                                $mdToast.showSimple("Welcome back " + answer.username + "!");
                            }
                            else if (answer.action == "register") {
                                console.log(answer.username + " is now registered.");
                                $mdToast.showSimple("Welcome, " + answer.username + "!");
                            }
                            else {
                                console.log("There is a disturbance in the login mechanism...");
                            }
                        }, function() {
                            console.log('User decided to continue as a guest.');
                        });
                }
            };

            return service;
        }]);

function startSingleRun() {
	var mainCtrlScope = angular.element("#appShell").scope();
	var data = mainCtrlScope.content["singleRun"].packData();
	// Make algorithm start request
}

function LoginController(AccountActionService, $scope, $mdDialog, $location, user,
                         pass, act, isLoading) {
    $scope.username = user;
    $scope.password = pass;
    $scope.act = act;
    $scope.isLoading = isLoading;
    $scope.errorMsg = "";

    $scope.hide = function() {
        $mdDialog.hide();
    };

    $scope.cancel = function() {
        $location.path('/');
        $mdDialog.cancel();
    };

    $scope.answer = function(answer) {
        var response = {};
        AccountActionService.authUser($scope.username, $scope.password, $scope.act,
         function(successResponse) {
            AccountActionService.updateUserInfo($scope.username, $scope.password, $scope.act);
            $location.path('/');
            response.username = $scope.username;
            response.password = $scope.password;
            response.action = $scope.act;
            $mdDialog.hide(response);
        }, function(errResponse) {
            console.log(errResponse);
        });
    };

    $scope.switchAction = function(newAction) {
        $scope.act = newAction;
        var title = ($scope.act == 'login') ? 'Log In' : 'Sign Up';
        $location.path('/' + $scope.act);
        //$window.history.replaceState({}, title, $scope.act);
    }

}

function generateSalt() {
    var salt = Math.random().toString(36).substring(2, 15) +
               Math.random().toString(36).substring(2, 15);
    return salt.toString();
}