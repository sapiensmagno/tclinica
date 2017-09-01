(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AccountantTccController', AccountantTccController);

    AccountantTccController.$inject = ['Accountant'];

    function AccountantTccController(Accountant) {

        var vm = this;

        vm.accountants = [];

        loadAll();

        function loadAll() {
            Accountant.query(function(result) {
                vm.accountants = result;
                vm.searchQuery = null;
            });
        }
    }
})();
