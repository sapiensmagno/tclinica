(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AppointmentTccDetailController', AppointmentTccDetailController);

    AppointmentTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Appointment', 'PaymentInstallment', 'Patient', 'DoctorSchedule'];

    function AppointmentTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Appointment, PaymentInstallment, Patient, DoctorSchedule) {
        var vm = this;

        vm.appointment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:appointmentUpdate', function(event, result) {
            vm.appointment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
