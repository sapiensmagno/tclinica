(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamStatusTccDialogController', ExamStatusTccDialogController);

    ExamStatusTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ExamStatus', 'Exam'];

    function ExamStatusTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ExamStatus, Exam) {
        var vm = this;

        vm.examStatus = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.exams = Exam.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.examStatus.id !== null) {
                ExamStatus.update(vm.examStatus, onSaveSuccess, onSaveError);
            } else {
                ExamStatus.save(vm.examStatus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:examStatusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
