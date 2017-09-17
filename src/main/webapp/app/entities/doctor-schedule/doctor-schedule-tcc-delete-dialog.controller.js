(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorScheduleTccDeleteController',DoctorScheduleTccDeleteController);

    DoctorScheduleTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'DoctorSchedule'];

    function DoctorScheduleTccDeleteController($uibModalInstance, entity, DoctorSchedule) {
        var vm = this;

        vm.doctorSchedule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DoctorSchedule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
