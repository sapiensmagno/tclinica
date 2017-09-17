(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('MedicalRecordTccController', MedicalRecordTccController);

    MedicalRecordTccController.$inject = ['MedicalRecord'];

    function MedicalRecordTccController(MedicalRecord) {

        var vm = this;

        vm.medicalRecords = [];

        loadAll();

        function loadAll() {
            MedicalRecord.query(function(result) {
                vm.medicalRecords = result;
                vm.searchQuery = null;
            });
        }
    }
})();
