(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AvailableWeekdaysTccDeleteController',AvailableWeekdaysTccDeleteController);

    AvailableWeekdaysTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'AvailableWeekdays'];

    function AvailableWeekdaysTccDeleteController($uibModalInstance, entity, AvailableWeekdays) {
        var vm = this;

        vm.availableWeekdays = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AvailableWeekdays.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
