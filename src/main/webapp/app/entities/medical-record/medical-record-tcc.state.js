(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medical-record-tcc', {
            parent: 'entity',
            url: '/medical-record-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.medicalRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-record/medical-recordsTCC.html',
                    controller: 'MedicalRecordTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicalRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medical-record-tcc-detail', {
            parent: 'medical-record-tcc',
            url: '/medical-record-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.medicalRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medical-record/medical-record-tcc-detail.html',
                    controller: 'MedicalRecordTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicalRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'MedicalRecord', function($stateParams, MedicalRecord) {
                    return MedicalRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medical-record-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medical-record-tcc-detail.edit', {
            parent: 'medical-record-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-record/medical-record-tcc-dialog.html',
                    controller: 'MedicalRecordTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalRecord', function(MedicalRecord) {
                            return MedicalRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-record-tcc.new', {
            parent: 'medical-record-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-record/medical-record-tcc-dialog.html',
                    controller: 'MedicalRecordTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medical-record-tcc', null, { reload: 'medical-record-tcc' });
                }, function() {
                    $state.go('medical-record-tcc');
                });
            }]
        })
        .state('medical-record-tcc.edit', {
            parent: 'medical-record-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-record/medical-record-tcc-dialog.html',
                    controller: 'MedicalRecordTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MedicalRecord', function(MedicalRecord) {
                            return MedicalRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-record-tcc', null, { reload: 'medical-record-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medical-record-tcc.delete', {
            parent: 'medical-record-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medical-record/medical-record-tcc-delete-dialog.html',
                    controller: 'MedicalRecordTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MedicalRecord', function(MedicalRecord) {
                            return MedicalRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medical-record-tcc', null, { reload: 'medical-record-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
