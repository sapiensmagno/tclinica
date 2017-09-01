(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('AvailableWeekdaysTccController', AvailableWeekdaysTccController);

    AvailableWeekdaysTccController.$inject = ['AvailableWeekdays'];

    function AvailableWeekdaysTccController(AvailableWeekdays) {

        var vm = this;

        vm.availableWeekdays = [];

        loadAll();

        function loadAll() {
            AvailableWeekdays.query(function(result) {
                vm.availableWeekdays = result;
                vm.searchQuery = null;
            });
        }
    }
})();
