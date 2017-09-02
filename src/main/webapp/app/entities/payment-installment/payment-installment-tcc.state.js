(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('payment-installment-tcc', {
            parent: 'entity',
            url: '/payment-installment-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.paymentInstallment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-installment/payment-installmentsTCC.html',
                    controller: 'PaymentInstallmentTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentInstallment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('payment-installment-tcc-detail', {
            parent: 'payment-installment-tcc',
            url: '/payment-installment-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.paymentInstallment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/payment-installment/payment-installment-tcc-detail.html',
                    controller: 'PaymentInstallmentTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('paymentInstallment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PaymentInstallment', function($stateParams, PaymentInstallment) {
                    return PaymentInstallment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'payment-installment-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('payment-installment-tcc-detail.edit', {
            parent: 'payment-installment-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-installment/payment-installment-tcc-dialog.html',
                    controller: 'PaymentInstallmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentInstallment', function(PaymentInstallment) {
                            return PaymentInstallment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-installment-tcc.new', {
            parent: 'payment-installment-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-installment/payment-installment-tcc-dialog.html',
                    controller: 'PaymentInstallmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                payDate: null,
                                dueDate: null,
                                value: null,
                                installmentNumber: null,
                                checkNumber: null,
                                cardFinalNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('payment-installment-tcc', null, { reload: 'payment-installment-tcc' });
                }, function() {
                    $state.go('payment-installment-tcc');
                });
            }]
        })
        .state('payment-installment-tcc.edit', {
            parent: 'payment-installment-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-installment/payment-installment-tcc-dialog.html',
                    controller: 'PaymentInstallmentTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PaymentInstallment', function(PaymentInstallment) {
                            return PaymentInstallment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-installment-tcc', null, { reload: 'payment-installment-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('payment-installment-tcc.delete', {
            parent: 'payment-installment-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/payment-installment/payment-installment-tcc-delete-dialog.html',
                    controller: 'PaymentInstallmentTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PaymentInstallment', function(PaymentInstallment) {
                            return PaymentInstallment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('payment-installment-tcc', null, { reload: 'payment-installment-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
