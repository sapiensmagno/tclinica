(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AvailableWeekdaysTccDialogController', AvailableWeekdaysTccDialogController);

    AvailableWeekdaysTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AvailableWeekdays', 'DoctorSchedule'];

    function AvailableWeekdaysTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AvailableWeekdays, DoctorSchedule) {
        var vm = this;

        vm.availableWeekdays = entity;
        vm.clear = clear;
        vm.save = save;
        vm.doctorschedules = DoctorSchedule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.availableWeekdays.id !== null) {
                AvailableWeekdays.update(vm.availableWeekdays, onSaveSuccess, onSaveError);
            } else {
                AvailableWeekdays.save(vm.availableWeekdays, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:availableWeekdaysUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
