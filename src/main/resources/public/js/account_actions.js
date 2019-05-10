'use strict';

angular.module('AccountAction')
    .factory('AccountActionService', ['$http', '$cookies', '$rootScope', '$q',
        function($http, $cookies, $rootScope, $q) {
            var service = {
                updateUserInfo: function() {

                },
                clearUserInfo: function() {

                },
                authUser: function(username, password, url, successCall, errorCall) {
                    //Salt password here
                    var pwd;
                    var salt;
                    var saltPromise;
                    if (url == "register"){
                        salt = generateSalt();
                        pwd = password;
                        pwd = pwd.concat(salt);
                        saltPromise = $q(function(resolve, reject) {
                            resolve({ data: { saltString: salt } });
                        });
                    }

                    if (url == "login"){
                        var salturl;
                        salturl = "/"+username+"/salt";
                        saltPromise = $http.get(salturl,{})

                        pwd = password;
                        pwd = pwd.concat(salt);
                    }

                    saltPromise.then(function(saltResponse) {
                        salt = saltResponse.data.saltString;
                        //console.log(salt);
                        /*Please do hash here*/
                        //salt = generateSalt();
                        var data = {
                            username: username,
                            password: pwd,
                            salt: salt,
                            userType: $rootScope.userTypes.REGULAR
                        };
                        //console.log(data);
                        $http.post(url, data)
                            .then(function(response) {
                                successCall(response);
                            }, function(response) {
                                errorCall(response);
                            });

                    }, function(saltError) {
                        errorCall(saltError);
                    });
                },
                logout: function() {
                    // Add username as data and potentially add callback on success/failure
                    $http.post('logout', {});
                }
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
                                userInfo.username = answer.username;
                                console.log(answer.username + " has logged in.");
                                $mdToast.showSimple("Welcome back " + answer.username + "!");
                            }
                            else if (answer.action == "signup") {
                                userInfo.username = answer.username;
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
            console.log(successResponse);
            $location.path('/');
            $mdDialog.hide(successResponse);
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