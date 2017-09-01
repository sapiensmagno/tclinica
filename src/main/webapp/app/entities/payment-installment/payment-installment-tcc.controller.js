(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentInstallmentTccController', PaymentInstallmentTccController);

    PaymentInstallmentTccController.$inject = ['PaymentInstallment'];

    function PaymentInstallmentTccController(PaymentInstallment) {

        var vm = this;

        vm.paymentInstallments = [];

        loadAll();

        function loadAll() {
            PaymentInstallment.query(function(result) {
                vm.paymentInstallments = result;
                vm.searchQuery = null;
            });
        }
    }
})();
