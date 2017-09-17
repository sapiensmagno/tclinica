(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PrescriptionTccDialogController', PrescriptionTccDialogController);

    PrescriptionTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Prescription', 'Medicine', 'MedicalRecord'];

    function PrescriptionTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Prescription, Medicine, MedicalRecord) {
        var vm = this;

        vm.prescription = entity;
        vm.clear = clear;
        vm.save = save;
        vm.medicines = Medicine.query();
        vm.medicalrecords = MedicalRecord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prescription.id !== null) {
                Prescription.update(vm.prescription, onSaveSuccess, onSaveError);
            } else {
                Prescription.save(vm.prescription, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:prescriptionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
