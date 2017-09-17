(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicalRecordTccDeleteController',MedicalRecordTccDeleteController);

    MedicalRecordTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'MedicalRecord'];

    function MedicalRecordTccDeleteController($uibModalInstance, entity, MedicalRecord) {
        var vm = this;

        vm.medicalRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MedicalRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
