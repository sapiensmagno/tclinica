(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PrescriptionTccDetailController', PrescriptionTccDetailController);

    PrescriptionTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Prescription', 'Medicine', 'MedicalRecord'];

    function PrescriptionTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Prescription, Medicine, MedicalRecord) {
        var vm = this;

        vm.prescription = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:prescriptionUpdate', function(event, result) {
            vm.prescription = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
