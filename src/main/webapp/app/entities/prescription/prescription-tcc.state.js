(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('prescription-tcc', {
            parent: 'entity',
            url: '/prescription-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.prescription.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescription/prescriptionsTCC.html',
                    controller: 'PrescriptionTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescription');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('prescription-tcc-detail', {
            parent: 'prescription-tcc',
            url: '/prescription-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.prescription.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/prescription/prescription-tcc-detail.html',
                    controller: 'PrescriptionTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('prescription');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Prescription', function($stateParams, Prescription) {
                    return Prescription.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'prescription-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('prescription-tcc-detail.edit', {
            parent: 'prescription-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-tcc-dialog.html',
                    controller: 'PrescriptionTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescription-tcc.new', {
            parent: 'prescription-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-tcc-dialog.html',
                    controller: 'PrescriptionTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                number: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('prescription-tcc', null, { reload: 'prescription-tcc' });
                }, function() {
                    $state.go('prescription-tcc');
                });
            }]
        })
        .state('prescription-tcc.edit', {
            parent: 'prescription-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-tcc-dialog.html',
                    controller: 'PrescriptionTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescription-tcc', null, { reload: 'prescription-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('prescription-tcc.delete', {
            parent: 'prescription-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/prescription/prescription-tcc-delete-dialog.html',
                    controller: 'PrescriptionTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Prescription', function(Prescription) {
                            return Prescription.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('prescription-tcc', null, { reload: 'prescription-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
