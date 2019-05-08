function login() {
	var app = 
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