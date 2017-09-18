(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Receptionist', Receptionist);

    Receptionist.$inject = ['$resource'];

    function Receptionist ($resource) {
        var resourceUrl =  'api/receptionists/:id';

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
