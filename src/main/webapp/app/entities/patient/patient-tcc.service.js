(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Patient', Patient);

    Patient.$inject = ['$resource'];

    function Patient ($resource) {
        var resourceUrl =  'api/patients/:id';

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
