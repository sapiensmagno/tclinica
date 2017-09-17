(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorTccDeleteController',DoctorTccDeleteController);

    DoctorTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Doctor'];

    function DoctorTccDeleteController($uibModalInstance, entity, Doctor) {
        var vm = this;

        vm.doctor = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Doctor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
