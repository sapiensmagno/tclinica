(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicalRecordTccDialogController', MedicalRecordTccDialogController);

    MedicalRecordTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MedicalRecord', 'Appointment', 'Exam', 'Prescription'];

    function MedicalRecordTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MedicalRecord, Appointment, Exam, Prescription) {
        var vm = this;

        vm.medicalRecord = entity;
        vm.clear = clear;
        vm.save = save;
        vm.appointments = Appointment.query({filter: 'medicalrecord-is-null'});
        $q.all([vm.medicalRecord.$promise, vm.appointments.$promise]).then(function() {
            if (!vm.medicalRecord.appointment || !vm.medicalRecord.appointment.id) {
                return $q.reject();
            }
            return Appointment.get({id : vm.medicalRecord.appointment.id}).$promise;
        }).then(function(appointment) {
            vm.appointments.push(appointment);
        });
        vm.exams = Exam.query();
        vm.prescriptions = Prescription.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.medicalRecord.id !== null) {
                MedicalRecord.update(vm.medicalRecord, onSaveSuccess, onSaveError);
            } else {
                MedicalRecord.save(vm.medicalRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:medicalRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
