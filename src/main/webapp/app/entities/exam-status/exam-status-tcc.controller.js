(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamStatusTccController', ExamStatusTccController);

    ExamStatusTccController.$inject = ['ExamStatus'];

    function ExamStatusTccController(ExamStatus) {

        var vm = this;

        vm.examStatuses = [];

        loadAll();

        function loadAll() {
            ExamStatus.query(function(result) {
                vm.examStatuses = result;
                vm.searchQuery = null;
            });
        }
    }
})();
