(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicineTccDialogController', MedicineTccDialogController);

    MedicineTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Medicine'];

    function MedicineTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Medicine) {
        var vm = this;

        vm.medicine = entity;
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
            if (vm.medicine.id !== null) {
                Medicine.update(vm.medicine, onSaveSuccess, onSaveError);
            } else {
                Medicine.save(vm.medicine, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:medicineUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
