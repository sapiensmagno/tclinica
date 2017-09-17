(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('HealthcareTccDialogController', HealthcareTccDialogController);

    HealthcareTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Healthcare', 'PaymentInstallment'];

    function HealthcareTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Healthcare, PaymentInstallment) {
        var vm = this;

        vm.healthcare = entity;
        vm.clear = clear;
        vm.save = save;
        vm.paymentinstallments = PaymentInstallment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.healthcare.id !== null) {
                Healthcare.update(vm.healthcare, onSaveSuccess, onSaveError);
            } else {
                Healthcare.save(vm.healthcare, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:healthcareUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
