(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('doctor-schedule-tcc', {
            parent: 'entity',
            url: '/doctor-schedule-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.doctorSchedule.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedulesTCC.html',
                    controller: 'DoctorScheduleTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doctorSchedule');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('doctor-schedule-tcc-detail', {
            parent: 'doctor-schedule-tcc',
            url: '/doctor-schedule-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.doctorSchedule.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedule-tcc-detail.html',
                    controller: 'DoctorScheduleTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('doctorSchedule');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DoctorSchedule', function($stateParams, DoctorSchedule) {
                    return DoctorSchedule.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'doctor-schedule-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('doctor-schedule-tcc-detail.edit', {
            parent: 'doctor-schedule-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedule-tcc-dialog.html',
                    controller: 'DoctorScheduleTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoctorSchedule', function(DoctorSchedule) {
                            return DoctorSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor-schedule-tcc.new', {
            parent: 'doctor-schedule-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedule-tcc-dialog.html',
                    controller: 'DoctorScheduleTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                appointmentsDurationMinutes: null,
                                intervalBetweenAppointmentsMinutes: null,
                                earliestAppointmentTime: null,
                                latestAppointmentTime: null,
                                calendarId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('doctor-schedule-tcc', null, { reload: 'doctor-schedule-tcc' });
                }, function() {
                    $state.go('doctor-schedule-tcc');
                });
            }]
        })
        .state('doctor-schedule-tcc.edit', {
            parent: 'doctor-schedule-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedule-tcc-dialog.html',
                    controller: 'DoctorScheduleTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DoctorSchedule', function(DoctorSchedule) {
                            return DoctorSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor-schedule-tcc', null, { reload: 'doctor-schedule-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('doctor-schedule-tcc.delete', {
            parent: 'doctor-schedule-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/doctor-schedule/doctor-schedule-tcc-delete-dialog.html',
                    controller: 'DoctorScheduleTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DoctorSchedule', function(DoctorSchedule) {
                            return DoctorSchedule.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('doctor-schedule-tcc', null, { reload: 'doctor-schedule-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
