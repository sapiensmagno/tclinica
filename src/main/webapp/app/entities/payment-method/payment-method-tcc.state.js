(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-method-tcc', {
            parent: 'entity',
            url: '/payment-method-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.paymentMethod.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-method/payment-methodsTCC.html',
                    controller: 'PaymentMethodTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentMethod');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payment-method-tcc-detail', {
            parent: 'payment-method-tcc',
            url: '/payment-method-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.paymentMethod.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-method/payment-method-tcc-detail.html',
                    controller: 'PaymentMethodTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentMethod');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaymentMethod', function($stateParams, PaymentMethod) {
                    return PaymentMethod.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-method-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-method-tcc-detail.edit', {
            parent: 'payment-method-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-method/payment-method-tcc-dialog.html',
                    controller: 'PaymentMethodTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentMethod', function(PaymentMethod) {
                            return PaymentMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-method-tcc.new', {
            parent: 'payment-method-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-method/payment-method-tcc-dialog.html',
                    controller: 'PaymentMethodTccDialogController',
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
                    $state.go('payment-method-tcc', null, { reload: 'payment-method-tcc' });
                }, function() {
                    $state.go('payment-method-tcc');
                });
            }]
        })
        .state('payment-method-tcc.edit', {
            parent: 'payment-method-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-method/payment-method-tcc-dialog.html',
                    controller: 'PaymentMethodTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentMethod', function(PaymentMethod) {
                            return PaymentMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-method-tcc', null, { reload: 'payment-method-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-method-tcc.delete', {
            parent: 'payment-method-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-method/payment-method-tcc-delete-dialog.html',
                    controller: 'PaymentMethodTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentMethod', function(PaymentMethod) {
                            return PaymentMethod.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-method-tcc', null, { reload: 'payment-method-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
