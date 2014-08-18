(function() {
    var app = angular.module('search', []);

	app.controller('SearchController', ['$scope', '$http', function($scope, $http) {
		var results = this,
			search = '';
		
		results.dspace = [];
		results.data = [];
		
		this.getResults = function() {
			search = $scope.search;
			
            $http.jsonp('http://api.duckduckgo.com/?q=' + search + '&format=json&pretty=1&callback=JSON_CALLBACK').success(function(data) {
                results.data = data;
            });

            
        };
		
	}]);
    
    /*
    app.controller('StatsController', ['$http', function($http){
        var stats = this;
        stats.dspace = [];
        stats.yadda = [];
        stats.ok = [];


        this.getDspace = function() {
            $http.get("/api/dspaceStats").success(function(data) {
                stats.dspace = data;
            });
        };

        this.getYadda = function() {
            $http.get("/api/yaddaStats").success(function(data) {
                stats.yadda = data;
            });
        };

        this.getOk = function() {
            $http.get("/api/okStats").success(function(data) {
                stats.ok = data;
            });
        };

        this.getDspace();

    }]);

    
    
    app.controller('RequestController', ['$scope', '$http', function($scope, $http) {
        var response = this,
            dbSave;
        response.array = [];
        response.array["dspace"] = [];
        response.array["yadda"] = [];
        response.array["ok"] = [];
        response.ok = [];

        this.request = function(serviceName) {
            $("#panel-" + serviceName).removeClass("panel-danger panel-success");

            switch(serviceName) {
                case "dspace":
                    dbSave = $scope.dbSaveDspace;
                    break;
                case "yadda":
                    dbSave = $scope.dbSaveYadda;
                    break;
                case "ok":
                    dbSave = $scope.dbSaveOk;
                    break;
            }

            $http.post("/execute", {service: serviceName, dbSave: dbSave}, { headers: {'Content-Type': 'application/json'}})
                .success(function(data) {
                    if (serviceName === "dspace") {
                        response.array[serviceName] = data.execute.dspace;
                    } else if (serviceName === "yadda") {
                        response.array[serviceName] = "Pozyskanie danych przebiegło pomyślnie. Liczba artykułów pełno tekstowych wynosi: " + data.execute.yadda;
                    } else if (serviceName === "ok") {
                        response.array[serviceName] = "Pozyskanie danych przebiegło pomyślnie.";
                        response.ok = data.execute.ok;
                    }

                    response.array[serviceName].message = "Pozyskanie danych przebiegło pomyślnie.";
                    $("#panel-" + serviceName).addClass("panel-success");
                })
                .error(function(data){
                    response.array[serviceName].message = "Błąd pozyskiwania danych.";
                    $("#panel-" + serviceName).addClass("panel-danger");
                });
        };

    }]);
*/


})();
