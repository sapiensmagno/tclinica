(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('accountant-tcc', {
            parent: 'entity',
            url: '/accountant-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.accountant.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accountant/accountantsTCC.html',
                    controller: 'AccountantTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountant');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('accountant-tcc-detail', {
            parent: 'accountant-tcc',
            url: '/accountant-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.accountant.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/accountant/accountant-tcc-detail.html',
                    controller: 'AccountantTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('accountant');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Accountant', function($stateParams, Accountant) {
                    return Accountant.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'accountant-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('accountant-tcc-detail.edit', {
            parent: 'accountant-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accountant/accountant-tcc-dialog.html',
                    controller: 'AccountantTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Accountant', function(Accountant) {
                            return Accountant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accountant-tcc.new', {
            parent: 'accountant-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accountant/accountant-tcc-dialog.html',
                    controller: 'AccountantTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                nickname: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('accountant-tcc', null, { reload: 'accountant-tcc' });
                }, function() {
                    $state.go('accountant-tcc');
                });
            }]
        })
        .state('accountant-tcc.edit', {
            parent: 'accountant-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accountant/accountant-tcc-dialog.html',
                    controller: 'AccountantTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Accountant', function(Accountant) {
                            return Accountant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accountant-tcc', null, { reload: 'accountant-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('accountant-tcc.delete', {
            parent: 'accountant-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/accountant/accountant-tcc-delete-dialog.html',
                    controller: 'AccountantTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Accountant', function(Accountant) {
                            return Accountant.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('accountant-tcc', null, { reload: 'accountant-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
