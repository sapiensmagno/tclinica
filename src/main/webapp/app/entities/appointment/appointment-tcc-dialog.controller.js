(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AppointmentTccDialogController', AppointmentTccDialogController);

    AppointmentTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Appointment', 'PaymentInstallment', 'Patient', 'DoctorSchedule', 'MedicalRecord'];

    function AppointmentTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Appointment, PaymentInstallment, Patient, DoctorSchedule, MedicalRecord) {
        var vm = this;
        
        vm.hidePatientSelect = false;
        setDefaultPatient();
        function setDefaultPatient() {
            Patient.query((function(pat) {
                if (!entity.id>0) {
                	entity.patient = pat[0];
                }
                vm.hidePatientSelect = pat.length < 2;
            }));
        }

        vm.appointment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.paymentinstallments = PaymentInstallment.query();
        vm.patients = Patient.query();
        vm.doctorschedules = DoctorSchedule.query();
        vm.medicalrecords = MedicalRecord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.appointment.id !== null) {
                Appointment.update(vm.appointment, onSaveSuccess, onSaveError);
            } else {
            	vm.appointment.endDate = 0;
                Appointment.save(vm.appointment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:appointmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startDate = false;
        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
