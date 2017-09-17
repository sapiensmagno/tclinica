(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('HealthcareTccDetailController', HealthcareTccDetailController);

    HealthcareTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Healthcare', 'PaymentInstallment'];

    function HealthcareTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Healthcare, PaymentInstallment) {
        var vm = this;

        vm.healthcare = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:healthcareUpdate', function(event, result) {
            vm.healthcare = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
