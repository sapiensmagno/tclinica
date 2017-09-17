(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTypeTccDetailController', ExamTypeTccDetailController);

    ExamTypeTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ExamType'];

    function ExamTypeTccDetailController($scope, $rootScope, $stateParams, previousState, entity, ExamType) {
        var vm = this;

        vm.examType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:examTypeUpdate', function(event, result) {
            vm.examType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
