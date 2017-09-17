(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('CardBrandTccController', CardBrandTccController);

    CardBrandTccController.$inject = ['CardBrand'];

    function CardBrandTccController(CardBrand) {

        var vm = this;

        vm.cardBrands = [];

        loadAll();

        function loadAll() {
            CardBrand.query(function(result) {
                vm.cardBrands = result;
                vm.searchQuery = null;
            });
        }
    }
})();
