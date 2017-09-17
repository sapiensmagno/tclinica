(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('ExamStatus', ExamStatus);

    ExamStatus.$inject = ['$resource', 'DateUtils'];

    function ExamStatus ($resource, DateUtils) {
        var resourceUrl =  'api/exam-statuses/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.creationDate = DateUtils.convertDateTimeFromServer(data.creationDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
