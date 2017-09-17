(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentInstallmentTccDialogController', PaymentInstallmentTccDialogController);

    PaymentInstallmentTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'PaymentInstallment', 'PaymentMethod', 'CardBrand', 'Healthcare', 'Appointment'];

    function PaymentInstallmentTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, PaymentInstallment, PaymentMethod, CardBrand, Healthcare, Appointment) {
        var vm = this;

        vm.paymentInstallment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.paymentmethods = PaymentMethod.query({filter: 'paymentinstallment-is-null'});
        $q.all([vm.paymentInstallment.$promise, vm.paymentmethods.$promise]).then(function() {
            if (!vm.paymentInstallment.paymentMethod || !vm.paymentInstallment.paymentMethod.id) {
                return $q.reject();
            }
            return PaymentMethod.get({id : vm.paymentInstallment.paymentMethod.id}).$promise;
        }).then(function(paymentMethod) {
            vm.paymentmethods.push(paymentMethod);
        });
        vm.cardbrands = CardBrand.query({filter: 'paymentinstallment-is-null'});
        $q.all([vm.paymentInstallment.$promise, vm.cardbrands.$promise]).then(function() {
            if (!vm.paymentInstallment.cardBrand || !vm.paymentInstallment.cardBrand.id) {
                return $q.reject();
            }
            return CardBrand.get({id : vm.paymentInstallment.cardBrand.id}).$promise;
        }).then(function(cardBrand) {
            vm.cardbrands.push(cardBrand);
        });
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
