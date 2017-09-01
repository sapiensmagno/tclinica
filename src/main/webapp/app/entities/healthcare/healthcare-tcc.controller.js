(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('HealthcareTccController', HealthcareTccController);

    HealthcareTccController.$inject = ['Healthcare'];

    function HealthcareTccController(Healthcare) {

        var vm = this;

        vm.healthcares = [];

        loadAll();

        function loadAll() {
            Healthcare.query(function(result) {
                vm.healthcares = result;
                vm.searchQuery = null;
            });
        }
    }
})();
