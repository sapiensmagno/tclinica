(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentInstallmentTccDeleteController',PaymentInstallmentTccDeleteController);

    PaymentInstallmentTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'PaymentInstallment'];

    function PaymentInstallmentTccDeleteController($uibModalInstance, entity, PaymentInstallment) {
        var vm = this;

        vm.paymentInstallment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PaymentInstallment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
