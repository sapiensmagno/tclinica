(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PatientTccDeleteController',PatientTccDeleteController);

    PatientTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Patient'];

    function PatientTccDeleteController($uibModalInstance, entity, Patient) {
        var vm = this;

        vm.patient = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Patient.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
