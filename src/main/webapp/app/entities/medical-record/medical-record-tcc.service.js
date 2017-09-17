(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('MedicalRecord', MedicalRecord);

    MedicalRecord.$inject = ['$resource'];

    function MedicalRecord ($resource) {
        var resourceUrl =  'api/medical-records/:id';

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
