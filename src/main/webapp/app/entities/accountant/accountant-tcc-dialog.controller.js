(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AccountantTccDialogController', AccountantTccDialogController);

    AccountantTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Accountant', 'User'];

    function AccountantTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Accountant, User) {
        var vm = this;

        vm.accountant = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.accountant.id !== null) {
                Accountant.update(vm.accountant, onSaveSuccess, onSaveError);
            } else {
                Accountant.save(vm.accountant, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:accountantUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
