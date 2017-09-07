(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('AvailableWeekdays', AvailableWeekdays);

    AvailableWeekdays.$inject = ['$resource'];

    function AvailableWeekdays ($resource) {
        var resourceUrl =  'api/available-weekdays/:id';

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
            'update': { method:'PUT' },
            'deletar': { method:'DELETE', hasBody: true, headers: {"Content-Type": "application/json;charset=UTF-8"} }
        });
    }
})();
