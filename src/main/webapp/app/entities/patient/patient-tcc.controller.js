(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PatientTccController', PatientTccController);

    PatientTccController.$inject = ['Patient'];

    function PatientTccController(Patient) {

        var vm = this;

        vm.patients = [];

        loadAll();

        function loadAll() {
            Patient.query(function(result) {
                vm.patients = result;
                vm.searchQuery = null;
            });
        }
    }
})();
