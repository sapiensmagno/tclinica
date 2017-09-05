(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicalRecordTccDetailController', MedicalRecordTccDetailController);

    MedicalRecordTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MedicalRecord', 'Appointment', 'Exam', 'Prescription'];

    function MedicalRecordTccDetailController($scope, $rootScope, $stateParams, previousState, entity, MedicalRecord, Appointment, Exam, Prescription) {
        var vm = this;

        vm.medicalRecord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:medicalRecordUpdate', function(event, result) {
            vm.medicalRecord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
