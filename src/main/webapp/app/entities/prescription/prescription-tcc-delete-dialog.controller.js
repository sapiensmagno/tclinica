(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PrescriptionTccDeleteController',PrescriptionTccDeleteController);

    PrescriptionTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Prescription'];

    function PrescriptionTccDeleteController($uibModalInstance, entity, Prescription) {
        var vm = this;

        vm.prescription = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Prescription.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
