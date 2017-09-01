(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorTccDialogController', DoctorTccDialogController);

    DoctorTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Doctor', 'User', 'DoctorSchedule'];

    function DoctorTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Doctor, User, DoctorSchedule) {
        var vm = this;

        vm.doctor = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.doctorschedules = DoctorSchedule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.doctor.id !== null) {
                Doctor.update(vm.doctor, onSaveSuccess, onSaveError);
            } else {
                Doctor.save(vm.doctor, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:doctorUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
