(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('healthcare-tcc', {
            parent: 'entity',
            url: '/healthcare-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.healthcare.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/healthcare/healthcaresTCC.html',
                    controller: 'HealthcareTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('healthcare');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('healthcare-tcc-detail', {
            parent: 'healthcare-tcc',
            url: '/healthcare-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.healthcare.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/healthcare/healthcare-tcc-detail.html',
                    controller: 'HealthcareTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('healthcare');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Healthcare', function($stateParams, Healthcare) {
                    return Healthcare.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'healthcare-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('healthcare-tcc-detail.edit', {
            parent: 'healthcare-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/healthcare/healthcare-tcc-dialog.html',
                    controller: 'HealthcareTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Healthcare', function(Healthcare) {
                            return Healthcare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('healthcare-tcc.new', {
            parent: 'healthcare-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/healthcare/healthcare-tcc-dialog.html',
                    controller: 'HealthcareTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                inactive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('healthcare-tcc', null, { reload: 'healthcare-tcc' });
                }, function() {
                    $state.go('healthcare-tcc');
                });
            }]
        })
        .state('healthcare-tcc.edit', {
            parent: 'healthcare-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/healthcare/healthcare-tcc-dialog.html',
                    controller: 'HealthcareTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Healthcare', function(Healthcare) {
                            return Healthcare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('healthcare-tcc', null, { reload: 'healthcare-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('healthcare-tcc.delete', {
            parent: 'healthcare-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/healthcare/healthcare-tcc-delete-dialog.html',
                    controller: 'HealthcareTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Healthcare', function(Healthcare) {
                            return Healthcare.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('healthcare-tcc', null, { reload: 'healthcare-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
