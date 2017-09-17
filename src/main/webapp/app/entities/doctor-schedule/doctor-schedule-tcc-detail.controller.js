(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorScheduleTccDetailController', DoctorScheduleTccDetailController);

    DoctorScheduleTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DoctorSchedule', 'Doctor', 'AvailableWeekdays', 'Appointment'];

    function DoctorScheduleTccDetailController($scope, $rootScope, $stateParams, previousState, entity, DoctorSchedule, Doctor, AvailableWeekdays, Appointment) {
        var vm = this;

        vm.doctorSchedule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:doctorScheduleUpdate', function(event, result) {
            vm.doctorSchedule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
