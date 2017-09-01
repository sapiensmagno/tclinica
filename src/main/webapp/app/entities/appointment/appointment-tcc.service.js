(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('Appointment', Appointment);

    Appointment.$inject = ['$resource', 'DateUtils'];

    function Appointment ($resource, DateUtils) {
        var resourceUrl =  'api/appointments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.scheduledDate = DateUtils.convertDateTimeFromServer(data.scheduledDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
