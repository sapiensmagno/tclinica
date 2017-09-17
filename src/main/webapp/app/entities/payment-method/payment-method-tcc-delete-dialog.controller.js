(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentMethodTccDeleteController',PaymentMethodTccDeleteController);

    PaymentMethodTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentMethod'];

    function PaymentMethodTccDeleteController($uibModalInstance, entity, PaymentMethod) {
        var vm = this;

        vm.paymentMethod = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentMethod.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
