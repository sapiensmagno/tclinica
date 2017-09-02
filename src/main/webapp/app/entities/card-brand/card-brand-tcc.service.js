(function() {
    'use strict';
    angular
        .module('tclinicaApp')
        .factory('CardBrand', CardBrand);

    CardBrand.$inject = ['$resource'];

    function CardBrand ($resource) {
        var resourceUrl =  'api/card-brands/:id';

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
