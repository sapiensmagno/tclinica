(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('DoctorSchedule', DoctorSchedule);

    DoctorSchedule.$inject = ['$resource', 'DateUtils'];

    function DoctorSchedule ($resource, DateUtils) {
        var resourceUrl =  'api/doctor-schedules/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.earliestAppointmentTime = DateUtils.convertDateTimeFromServer(data.earliestAppointmentTime);
                        data.latestAppointmentTime = DateUtils.convertDateTimeFromServer(data.latestAppointmentTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
