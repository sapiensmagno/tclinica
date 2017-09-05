(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamStatusTccDeleteController',ExamStatusTccDeleteController);

    ExamStatusTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'ExamStatus'];

    function ExamStatusTccDeleteController($uibModalInstance, entity, ExamStatus) {
        var vm = this;

        vm.examStatus = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ExamStatus.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
