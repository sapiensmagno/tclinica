(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('receptionist', {
            parent: 'entity',
            url: '/receptionist',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.receptionist.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/receptionist/receptionists.html',
                    controller: 'ReceptionistController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('receptionist');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('receptionist-detail', {
            parent: 'receptionist',
            url: '/receptionist/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.receptionist.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/receptionist/receptionist-detail.html',
                    controller: 'ReceptionistDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('receptionist');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Receptionist', function($stateParams, Receptionist) {
                    return Receptionist.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'receptionist',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('receptionist-detail.edit', {
            parent: 'receptionist-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/receptionist/receptionist-dialog.html',
                    controller: 'ReceptionistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Receptionist', function(Receptionist) {
                            return Receptionist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('receptionist.new', {
            parent: 'receptionist',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/receptionist/receptionist-dialog.html',
                    controller: 'ReceptionistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nickname: null,
                                inactive: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('receptionist', null, { reload: 'receptionist' });
                }, function() {
                    $state.go('receptionist');
                });
            }]
        })
        .state('receptionist.edit', {
            parent: 'receptionist',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/receptionist/receptionist-dialog.html',
                    controller: 'ReceptionistDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Receptionist', function(Receptionist) {
                            return Receptionist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('receptionist', null, { reload: 'receptionist' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('receptionist.delete', {
            parent: 'receptionist',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/receptionist/receptionist-delete-dialog.html',
                    controller: 'ReceptionistDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Receptionist', function(Receptionist) {
                            return Receptionist.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('receptionist', null, { reload: 'receptionist' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
