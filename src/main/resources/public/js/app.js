var barApp = angular.module('barApp', [ 'ngRoute', 'ui.bootstrap' ]);

barApp.config(['$routeProvider',
	function($routeProvider) {
		$routeProvider.
			when('/ingredients', {
				templateUrl: 'views/ingredients.html',
				controller: 'IngredientListCtrl as ingredientListCtrl'
			}).
			when('/cocktails', {
				templateUrl: 'views/cocktails.html'
				// controller: 'CocktailListCtrl as cocktailListCtrl'
			}).
			otherwise({
				redirectTo: '/ingredients'
			});
}]);

barApp.controller('IngredientListCtrl', function ($log, $uibModal, $http) {
	
	var self = this;
	
  this.ingredients = [];
  
  $http.get('/rest/ingredients').then(function(resp) {
	  self.ingredients = resp.data;
  });
  
  this.selectedIngredient = null;
  this.selectIngredient = function(ingredient) {
	  this.selectedIngredient = ingredient;
  }
  
  this.addIngredient = function() {
	  this.openIngredientModal(true, null);
  }
  
  this.editIngredient = function() {
	  this.openIngredientModal(false, this.selectedIngredient);
  }
  
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
	    	$log.info('Modal ok with ' + selectedItem);
	    }, function () {
	    	$log.info('Modal dismissed at: ' + new Date());
	    });
  }
  
  this.deleteIngredient = function() {
	  alert("delete ingredient");
  }
  
});

barApp.controller('IngredientEditCtrl', function ($modalInstance, isCreateMode, ingredient) {
	this.title = isCreateMode ? "Add Ingredient" : "Edit Ingredient";
	this.ingredient = isCreateMode ? {} : ingredient;
	
	this.ok = function () {
		$modalInstance.close(this.ingredient);
	};

	this.cancel = function () {
		$modalInstance.dismiss('cancel');
	};
});
