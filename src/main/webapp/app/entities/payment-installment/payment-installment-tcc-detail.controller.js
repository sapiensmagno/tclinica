(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentInstallmentTccDetailController', PaymentInstallmentTccDetailController);

    PaymentInstallmentTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PaymentInstallment', 'PaymentMethod', 'CardBrand', 'Healthcare', 'Appointment'];

    function PaymentInstallmentTccDetailController($scope, $rootScope, $stateParams, previousState, entity, PaymentInstallment, PaymentMethod, CardBrand, Healthcare, Appointment) {
        var vm = this;

        vm.paymentInstallment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:paymentInstallmentUpdate', function(event, result) {
            vm.paymentInstallment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
