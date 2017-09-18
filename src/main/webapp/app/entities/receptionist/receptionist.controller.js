(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('ReceptionistController', ReceptionistController);

    ReceptionistController.$inject = ['Receptionist'];

    function ReceptionistController(Receptionist) {

        var vm = this;

        vm.receptionists = [];

        loadAll();

        function loadAll() {
            Receptionist.query(function(result) {
                vm.receptionists = result;
                vm.searchQuery = null;
            });
        }
    }
})();
