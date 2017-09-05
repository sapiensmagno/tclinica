(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicineTccDetailController', MedicineTccDetailController);

    MedicineTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Medicine'];

    function MedicineTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Medicine) {
        var vm = this;

        vm.medicine = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:medicineUpdate', function(event, result) {
            vm.medicine = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
