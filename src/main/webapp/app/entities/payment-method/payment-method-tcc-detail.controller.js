(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentMethodTccDetailController', PaymentMethodTccDetailController);

    PaymentMethodTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentMethod'];

    function PaymentMethodTccDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentMethod) {
        var vm = this;

        vm.paymentMethod = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:paymentMethodUpdate', function(event, result) {
            vm.paymentMethod = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
