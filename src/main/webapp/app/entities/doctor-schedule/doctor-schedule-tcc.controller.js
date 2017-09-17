(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorScheduleTccController', DoctorScheduleTccController);

    DoctorScheduleTccController.$inject = ['DoctorSchedule'];

    function DoctorScheduleTccController(DoctorSchedule) {

        var vm = this;

        vm.doctorSchedules = [];

        loadAll();

        function loadAll() {
            DoctorSchedule.query(function(result) {
                vm.doctorSchedules = result;
                vm.searchQuery = null;
            });
        }
    }
})();
