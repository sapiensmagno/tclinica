(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Healthcare', Healthcare);

    Healthcare.$inject = ['$resource'];

    function Healthcare ($resource) {
        var resourceUrl =  'api/healthcares/:id';

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
