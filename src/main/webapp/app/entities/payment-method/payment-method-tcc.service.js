(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('PaymentMethod', PaymentMethod);

    PaymentMethod.$inject = ['$resource'];

    function PaymentMethod ($resource) {
        var resourceUrl =  'api/payment-methods/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
