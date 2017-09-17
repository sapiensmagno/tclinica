(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('CardBrandTccDetailController', CardBrandTccDetailController);

    CardBrandTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CardBrand'];

    function CardBrandTccDetailController($scope, $rootScope, $stateParams, previousState, entity, CardBrand) {
        var vm = this;

        vm.cardBrand = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:cardBrandUpdate', function(event, result) {
            vm.cardBrand = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
