(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ReceptionistDeleteController',ReceptionistDeleteController);

    ReceptionistDeleteController.$inject = ['$uibModalInstance', 'entity', 'Receptionist'];

    function ReceptionistDeleteController($uibModalInstance, entity, Receptionist) {
        var vm = this;

        vm.receptionist = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Receptionist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
