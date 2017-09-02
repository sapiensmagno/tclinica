(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('CardBrandTccDialogController', CardBrandTccDialogController);

    CardBrandTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CardBrand'];

    function CardBrandTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CardBrand) {
        var vm = this;

        vm.cardBrand = entity;
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
            if (vm.cardBrand.id !== null) {
                CardBrand.update(vm.cardBrand, onSaveSuccess, onSaveError);
            } else {
                CardBrand.save(vm.cardBrand, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:cardBrandUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
