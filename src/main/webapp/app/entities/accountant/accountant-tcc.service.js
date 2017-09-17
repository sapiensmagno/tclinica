(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Accountant', Accountant);

    Accountant.$inject = ['$resource'];

    function Accountant ($resource) {
        var resourceUrl =  'api/accountants/:id';

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
