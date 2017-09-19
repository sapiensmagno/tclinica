(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ExamTccController', ExamTccController);

    ExamTccController.$inject = ['Exam'];

    function ExamTccController(Exam) {

        var vm = this;
        vm.exams = [];

        loadAll();
        
        function loadAll() {
            Exam.query(function(result) {
                vm.exams = result;
                vm.searchQuery = null;
            });
        }
    }
})();
