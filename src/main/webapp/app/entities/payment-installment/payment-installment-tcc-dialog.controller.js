(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentInstallmentTccDialogController', PaymentInstallmentTccDialogController);

    PaymentInstallmentTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentInstallment', 'Healthcare', 'Appointment'];

    function PaymentInstallmentTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentInstallment, Healthcare, Appointment) {
        var vm = this;

        vm.paymentInstallment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.healthcares = Healthcare.query();
        vm.appointments = Appointment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paymentInstallment.id !== null) {
                PaymentInstallment.update(vm.paymentInstallment, onSaveSuccess, onSaveError);
            } else {
                PaymentInstallment.save(vm.paymentInstallment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:paymentInstallmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.payDate = false;
        vm.datePickerOpenStatus.dueDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
