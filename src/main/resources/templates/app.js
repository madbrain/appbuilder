var app = angular.module('app', [ 'ngRoute', 'ui.bootstrap' ]);

app.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider
{{#routes}}
{{#items}}
			.when('/{{url}}', {
				templateUrl: 'views/{{url}}.html',
				controller: '{{controller}} as {{controllerVar}}'
			})
{{/items}}
			.otherwise({
				redirectTo: '/{{defaultRouteUrl}}'
			});
{{/routes}}
}]);

app.controller('IngredientListCtrl', function ($log, $uibModal, $http) {
	
  var self = this;

  this.ingredients = [];
  
  this.selectedIngredient = null;
  this.selectIngredient = function(ingredient) {
	  this.selectedIngredient = ingredient;
  }
  
  this.loadIngredients = function() {
	  $http.get('/rest/ingredients').then(function(resp) {
		  self.ingredients = resp.data;
		  self.selectedIngredient = null;
	  });
  };
  
  this.addIngredient = function() {
	  this.openIngredientModal(true, null);
  };
  
  this.editIngredient = function() {
	  this.openIngredientModal(false, angular.copy(this.selectedIngredient));
  };
  
  this.openIngredientModal = function(isCreateMode, ingredient) {
	  var modalInstance = $uibModal.open({
	      animation: false,
	      templateUrl: 'IngredientModalContent.html',
	      controller: 'IngredientEditCtrl as ingredientEditCtrl',
	      // size: { '', 'lg', 'sm' }
	      resolve: {
	    	  isCreateMode: isCreateMode,
	    	  ingredient: ingredient
	      }
	    });

	    modalInstance.result.then(function (selectedItem) {
	    	$http.post('/rest/ingredients', selectedItem).then(function(resp) {
	    		$log.info('Save ok ' + selectedItem);
	    		self.loadIngredients();
	    	}, function(resp) {
	    		$log.error('Save error ' + resp);
	    	});
	    }, function () {
	    	// $log.info('Modal dismissed at: ' + new Date());
	    });
  };
  
  this.deleteIngredient = function() {
	  alert("delete ingredient");
  };
  
  this.loadIngredients();
  
});

app.controller('IngredientEditCtrl', function ($modalInstance, isCreateMode, ingredient) {
	this.title = isCreateMode ? "Ajouter Ingredient" : "Editer Ingredient";
	this.ingredient = isCreateMode ? {} : ingredient;
	
	this.ok = function () {
		// TODO: validation
		$modalInstance.close(this.ingredient);
	};

	this.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
});

