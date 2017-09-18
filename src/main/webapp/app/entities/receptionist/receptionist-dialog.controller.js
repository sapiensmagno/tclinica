(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ReceptionistDialogController', ReceptionistDialogController);

    ReceptionistDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Receptionist', 'User'];

    function ReceptionistDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Receptionist, User) {
        var vm = this;

        vm.receptionist = entity;
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
            if (vm.receptionist.id !== null) {
                Receptionist.update(vm.receptionist, onSaveSuccess, onSaveError);
            } else {
                Receptionist.save(vm.receptionist, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:receptionistUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
