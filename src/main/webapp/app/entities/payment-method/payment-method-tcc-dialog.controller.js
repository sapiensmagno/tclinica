(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PaymentMethodTccDialogController', PaymentMethodTccDialogController);

    PaymentMethodTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PaymentMethod'];

    function PaymentMethodTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PaymentMethod) {
        var vm = this;

        vm.paymentMethod = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.paymentMethod.id !== null) {
                PaymentMethod.update(vm.paymentMethod, onSaveSuccess, onSaveError);
            } else {
                PaymentMethod.save(vm.paymentMethod, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:paymentMethodUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
