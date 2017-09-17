(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('CardBrandTccDeleteController',CardBrandTccDeleteController);

    CardBrandTccDeleteController.$inject = ['$uibModalInstance', 'entity', 'CardBrand'];

    function CardBrandTccDeleteController($uibModalInstance, entity, CardBrand) {
        var vm = this;

        vm.cardBrand = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CardBrand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
