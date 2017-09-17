(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamStatusTccDetailController', ExamStatusTccDetailController);

    ExamStatusTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExamStatus', 'Exam'];

    function ExamStatusTccDetailController($scope, $rootScope, $stateParams, previousState, entity, ExamStatus, Exam) {
        var vm = this;

        vm.examStatus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:examStatusUpdate', function(event, result) {
            vm.examStatus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
