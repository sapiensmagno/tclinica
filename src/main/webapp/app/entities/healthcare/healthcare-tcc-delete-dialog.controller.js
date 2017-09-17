(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('HealthcareTccDeleteController',HealthcareTccDeleteController);

    HealthcareTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Healthcare'];

    function HealthcareTccDeleteController($uibModalInstance, entity, Healthcare) {
        var vm = this;

        vm.healthcare = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Healthcare.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
