(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('PaymentInstallment', PaymentInstallment);

    PaymentInstallment.$inject = ['$resource', 'DateUtils'];

    function PaymentInstallment ($resource, DateUtils) {
        var resourceUrl =  'api/payment-installments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.payDate = DateUtils.convertDateTimeFromServer(data.payDate);
                        data.dueDate = DateUtils.convertDateTimeFromServer(data.dueDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
