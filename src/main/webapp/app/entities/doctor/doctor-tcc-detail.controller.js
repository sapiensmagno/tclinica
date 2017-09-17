(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorTccDetailController', DoctorTccDetailController);

    DoctorTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Doctor', 'User', 'DoctorSchedule'];

    function DoctorTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Doctor, User, DoctorSchedule) {
        var vm = this;

        vm.doctor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:doctorUpdate', function(event, result) {
            vm.doctor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
