(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicineTccDeleteController',MedicineTccDeleteController);

    MedicineTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Medicine'];

    function MedicineTccDeleteController($uibModalInstance, entity, Medicine) {
        var vm = this;

        vm.medicine = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Medicine.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
