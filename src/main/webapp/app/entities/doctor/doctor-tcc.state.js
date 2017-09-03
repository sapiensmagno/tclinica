(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('doctor-tcc', {
            parent: 'entity',
            url: '/doctor-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.doctor.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor/doctorsTCC.html',
                    controller: 'DoctorTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doctor');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('doctor-tcc-detail', {
            parent: 'doctor-tcc',
            url: '/doctor-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.doctor.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor/doctor-tcc-detail.html',
                    controller: 'DoctorTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doctor');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Doctor', function($stateParams, Doctor) {
                    return Doctor.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'doctor-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('doctor-tcc-detail.edit', {
            parent: 'doctor-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-tcc-dialog.html',
                    controller: 'DoctorTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor-tcc.new', {
            parent: 'doctor-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-tcc-dialog.html',
                    controller: 'DoctorTccDialogController',
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
                    $state.go('doctor-tcc', null, { reload: 'doctor-tcc' });
                }, function() {
                    $state.go('doctor-tcc');
                });
            }]
        })
        .state('doctor-tcc.edit', {
            parent: 'doctor-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-tcc-dialog.html',
                    controller: 'DoctorTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor-tcc', null, { reload: 'doctor-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor-tcc.delete', {
            parent: 'doctor-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor/doctor-tcc-delete-dialog.html',
                    controller: 'DoctorTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Doctor', function(Doctor) {
                            return Doctor.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor-tcc', null, { reload: 'doctor-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
