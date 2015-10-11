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

barApp.controller('IngredientListCtrl', function ($log, $uibModal) {
	
	var self = this;
	
  this.ingredients = [
    {'name': 'Banane', 'unite': 'PIECE', 'tauxAlcool': 0.0, 'origine': 'Martinique', 'coutUnitaire': 0.5 },
    {'name': 'Limonade', 'unite': 'LITRE', 'tauxAlcool': 0.0, 'origine': '', 'coutUnitaire': 1 },
    {'name': 'Sel', 'unite': 'PINCEE', 'tauxAlcool': 0.0, 'origine': '', 'coutUnitaire': 0.1 },
    {'name': 'Ricard', 'unite': 'LITRE', 'tauxAlcool': 45.0, 'origine': 'France', 'coutUnitaire': 30.0 },
  ];
  
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
