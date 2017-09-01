(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AvailableWeekdaysTccDetailController', AvailableWeekdaysTccDetailController);

    AvailableWeekdaysTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AvailableWeekdays', 'DoctorSchedule'];

    function AvailableWeekdaysTccDetailController($scope, $rootScope, $stateParams, previousState, entity, AvailableWeekdays, DoctorSchedule) {
        var vm = this;

        vm.availableWeekdays = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:availableWeekdaysUpdate', function(event, result) {
            vm.availableWeekdays = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
