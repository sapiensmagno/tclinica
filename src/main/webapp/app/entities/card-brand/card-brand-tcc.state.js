(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('card-brand-tcc', {
            parent: 'entity',
            url: '/card-brand-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.cardBrand.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/card-brand/card-brandsTCC.html',
                    controller: 'CardBrandTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cardBrand');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('card-brand-tcc-detail', {
            parent: 'card-brand-tcc',
            url: '/card-brand-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.cardBrand.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/card-brand/card-brand-tcc-detail.html',
                    controller: 'CardBrandTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('cardBrand');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CardBrand', function($stateParams, CardBrand) {
                    return CardBrand.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'card-brand-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('card-brand-tcc-detail.edit', {
            parent: 'card-brand-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card-brand/card-brand-tcc-dialog.html',
                    controller: 'CardBrandTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CardBrand', function(CardBrand) {
                            return CardBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('card-brand-tcc.new', {
            parent: 'card-brand-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card-brand/card-brand-tcc-dialog.html',
                    controller: 'CardBrandTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('card-brand-tcc', null, { reload: 'card-brand-tcc' });
                }, function() {
                    $state.go('card-brand-tcc');
                });
            }]
        })
        .state('card-brand-tcc.edit', {
            parent: 'card-brand-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card-brand/card-brand-tcc-dialog.html',
                    controller: 'CardBrandTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CardBrand', function(CardBrand) {
                            return CardBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('card-brand-tcc', null, { reload: 'card-brand-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('card-brand-tcc.delete', {
            parent: 'card-brand-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/card-brand/card-brand-tcc-delete-dialog.html',
                    controller: 'CardBrandTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CardBrand', function(CardBrand) {
                            return CardBrand.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('card-brand-tcc', null, { reload: 'card-brand-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
