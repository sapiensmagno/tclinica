(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('appointment-tcc', {
            parent: 'entity',
            url: '/appointment-tcc?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.appointment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appointment/appointmentstcc.html',
                    controller: 'AppointmentTccController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appointment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('appointment-tcc-detail', {
            parent: 'appointment-tcc',
            url: '/appointment-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.appointment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/appointment/appointment-tcc-detail.html',
                    controller: 'AppointmentTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('appointment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Appointment', function($stateParams, Appointment) {
                    return Appointment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'appointment-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('appointment-tcc-detail.edit', {
            parent: 'appointment-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-tcc-dialog.html',
                    controller: 'AppointmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appointment-tcc.new', {
            parent: 'appointment-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-tcc-dialog.html',
                    controller: 'AppointmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                scheduledDate: null,
                                cancelled: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('appointment-tcc', null, { reload: 'appointment-tcc' });
                }, function() {
                    $state.go('appointment-tcc');
                });
            }]
        })
        .state('appointment-tcc.edit', {
            parent: 'appointment-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-tcc-dialog.html',
                    controller: 'AppointmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appointment-tcc', null, { reload: 'appointment-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('appointment-tcc.delete', {
            parent: 'appointment-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/appointment/appointment-tcc-delete-dialog.html',
                    controller: 'AppointmentTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Appointment', function(Appointment) {
                            return Appointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('appointment-tcc', null, { reload: 'appointment-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
