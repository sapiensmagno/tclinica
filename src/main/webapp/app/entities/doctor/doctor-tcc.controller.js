(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorTccController', DoctorTccController);

    DoctorTccController.$inject = ['Doctor'];

    function DoctorTccController(Doctor) {

        var vm = this;

        vm.doctors = [];

        loadAll();

        function loadAll() {
            Doctor.query(function(result) {
                vm.doctors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
