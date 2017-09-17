(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AccountantTccDetailController', AccountantTccDetailController);

    AccountantTccDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Accountant', 'User'];

    function AccountantTccDetailController($scope, $rootScope, $stateParams, previousState, entity, Accountant, User) {
        var vm = this;

        vm.accountant = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('tclinicaApp:accountantUpdate', function(event, result) {
            vm.accountant = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
