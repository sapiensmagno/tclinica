(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Exam', Exam);

    Exam.$inject = ['$resource'];

    function Exam ($resource) {
        var resourceUrl =  'api/exams/:id';

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
