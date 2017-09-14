(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('available-weekdays-tcc', {
            parent: 'entity',
            url: '/available-weekdays-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.availableWeekdays.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/available-weekdays/available-weekdaysTCC.html',
                    controller: 'AvailableWeekdaysTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('availableWeekdays');
                    $translatePartialLoader.addPart('DayOfWeek');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('available-weekdays-tcc-detail', {
            parent: 'available-weekdays-tcc',
            url: '/available-weekdays-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.availableWeekdays.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/available-weekdays/available-weekdays-tcc-detail.html',
                    controller: 'AvailableWeekdaysTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('availableWeekdays');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AvailableWeekdays', function($stateParams, AvailableWeekdays) {
                    return AvailableWeekdays.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'available-weekdays-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('available-weekdays-tcc-detail.edit', {
            parent: 'available-weekdays-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available-weekdays/available-weekdays-tcc-dialog.html',
                    controller: 'AvailableWeekdaysTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AvailableWeekdays', function(AvailableWeekdays) {
                            return AvailableWeekdays.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('available-weekdays-tcc.new', {
            parent: 'available-weekdays-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available-weekdays/available-weekdays-tcc-dialog.html',
                    controller: 'AvailableWeekdaysTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                weekday: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('available-weekdays-tcc', null, { reload: 'available-weekdays-tcc' });
                }, function() {
                    $state.go('available-weekdays-tcc');
                });
            }]
        })
        .state('available-weekdays-tcc.edit', {
            parent: 'available-weekdays-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available-weekdays/available-weekdays-tcc-dialog.html',
                    controller: 'AvailableWeekdaysTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AvailableWeekdays', function(AvailableWeekdays) {
                            return AvailableWeekdays.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('available-weekdays-tcc', null, { reload: 'available-weekdays-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('available-weekdays-tcc.delete', {
            parent: 'available-weekdays-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available-weekdays/available-weekdays-tcc-delete-dialog.html',
                    controller: 'AvailableWeekdaysTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AvailableWeekdays', function(AvailableWeekdays) {
                            return AvailableWeekdays.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('available-weekdays-tcc', null, { reload: 'available-weekdays-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
