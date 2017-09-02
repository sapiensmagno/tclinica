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
                        data.payDate = DateUtils.convertLocalDateFromServer(data.payDate);
                        data.dueDate = DateUtils.convertLocalDateFromServer(data.dueDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.payDate = DateUtils.convertLocalDateToServer(copy.payDate);
                    copy.dueDate = DateUtils.convertLocalDateToServer(copy.dueDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.payDate = DateUtils.convertLocalDateToServer(copy.payDate);
                    copy.dueDate = DateUtils.convertLocalDateToServer(copy.dueDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
