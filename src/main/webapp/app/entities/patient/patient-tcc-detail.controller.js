(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PatientTccDetailController', PatientTccDetailController);

    PatientTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Patient', 'User'];

    function PatientTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Patient, User) {
        var vm = this;

        vm.patient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:patientUpdate', function(event, result) {
            vm.patient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
