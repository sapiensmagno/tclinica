(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTccDetailController', ExamTccDetailController);

    ExamTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Exam', 'ExamType', 'ExamStatus', 'MedicalRecord'];

    function ExamTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Exam, ExamType, ExamStatus, MedicalRecord) {
        var vm = this;

        vm.exam = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:examUpdate', function(event, result) {
            vm.exam = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
