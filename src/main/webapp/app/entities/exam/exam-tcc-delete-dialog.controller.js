(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTccDeleteController',ExamTccDeleteController);

    ExamTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'Exam'];

    function ExamTccDeleteController($uibModalInstance, entity, Exam) {
        var vm = this;

        vm.exam = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Exam.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
