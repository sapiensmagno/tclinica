(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentMethodTccController', PaymentMethodTccController);

    PaymentMethodTccController.$inject = ['PaymentMethod'];

    function PaymentMethodTccController(PaymentMethod) {

        var vm = this;

        vm.paymentMethods = [];

        loadAll();

        function loadAll() {
            PaymentMethod.query(function(result) {
                vm.paymentMethods = result;
                vm.searchQuery = null;
            });
        }
    }
})();
