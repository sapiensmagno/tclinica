(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('PrescriptionTccController', PrescriptionTccController);

    PrescriptionTccController.$inject = ['Prescription'];

    function PrescriptionTccController(Prescription) {

        var vm = this;

        vm.prescriptions = [];

        loadAll();

        function loadAll() {
            Prescription.query(function(result) {
                vm.prescriptions = result;
                vm.searchQuery = null;
            });
        }
    }
})();
