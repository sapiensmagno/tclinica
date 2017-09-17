(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AccountantTccDeleteController',AccountantTccDeleteController);

    AccountantTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Accountant'];

    function AccountantTccDeleteController($uibModalInstance, entity, Accountant) {
        var vm = this;

        vm.accountant = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Accountant.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
