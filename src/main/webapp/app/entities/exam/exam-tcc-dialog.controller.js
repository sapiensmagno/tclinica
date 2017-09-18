(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTccDialogController', ExamTccDialogController);

    ExamTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Exam', 'ExamType', 'ExamStatus', 'MedicalRecord'];

    function ExamTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Exam, ExamType, ExamStatus, MedicalRecord) {
        var vm = this;

        vm.exam = entity;
        vm.clear = clear;
        vm.save = save;
        vm.labRequest = labRequest;
        vm.toDoctor = toDoctor;
        vm.toPatient = toPatient;
        vm.archive = archive;
        vm.examtypes = ExamType.query({filter: 'exam-is-null'});
        $q.all([vm.exam.$promise, vm.examtypes.$promise]).then(function() {
            if (!vm.exam.examType || !vm.exam.examType.id) {
                return $q.reject();
            }
            return ExamType.get({id : vm.exam.examType.id}).$promise;
        }).then(function(examType) {
            vm.examtypes.push(examType);
        });
        vm.examstatuses = ExamStatus.query();
        vm.medicalrecords = MedicalRecord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }
        
        function labRequest () {
        	vm.isSaving = true;
        	if (vm.exam.id !== null) {
        		ExamStatus.labRequest(vm.exam, onSaveSuccess, onSaveError);
        	}
        }
        
        function toDoctor () {
        	vm.isSaving = true;
        	if (vm.exam.id !== null) {
        		ExamStatus.toDoctor(vm.exam, onSaveSuccess, onSaveError);
        	}
        }
        
        function toPatient () {
        	vm.isSaving = true;
        	if (vm.exam.id !== null) {
        		ExamStatus.toPatient(vm.exam, onSaveSuccess, onSaveError);
        	}
        }
        
        function archive () {
        	vm.isSaving = true;
        	if (vm.exam.id !== null) {
        		ExamStatus.archive(vm.exam, onSaveSuccess, onSaveError);
        	}
        }
        
        function save () {
            vm.isSaving = true;
            if (vm.exam.id !== null) {
                Exam.update(vm.exam, onSaveSuccess, onSaveError);
            } else {
                Exam.save(vm.exam, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:examUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
