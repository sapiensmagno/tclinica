(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ReceptionistDetailController', ReceptionistDetailController);

    ReceptionistDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Receptionist', 'User'];

    function ReceptionistDetailController($scope, $rootScope, $stateParams, previousState, entity, Receptionist, User) {
        var vm = this;

        vm.receptionist = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:receptionistUpdate', function(event, result) {
            vm.receptionist = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
