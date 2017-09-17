(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTypeTccDeleteController',ExamTypeTccDeleteController);

    ExamTypeTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExamType'];

    function ExamTypeTccDeleteController($uibModalInstance, entity, ExamType) {
        var vm = this;

        vm.examType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExamType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
