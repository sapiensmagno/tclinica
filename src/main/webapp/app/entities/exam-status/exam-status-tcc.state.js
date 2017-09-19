(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exam-status-tcc', {
            parent: 'entity',
            url: '/exam-status-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.examStatus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam-status/exam-statusesTCC.html',
                    controller: 'ExamStatusTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examStatus');
                    $translatePartialLoader.addPart('ExamStatuses');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exam-status-tcc-detail', {
            parent: 'exam-status-tcc',
            url: '/exam-status-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.examStatus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam-status/exam-status-tcc-detail.html',
                    controller: 'ExamStatusTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExamStatus', function($stateParams, ExamStatus) {
                    return ExamStatus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exam-status-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exam-status-tcc-detail.edit', {
            parent: 'exam-status-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-status/exam-status-tcc-dialog.html',
                    controller: 'ExamStatusTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExamStatus', function(ExamStatus) {
                            return ExamStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-status-tcc.new', {
            parent: 'exam-status-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-status/exam-status-tcc-dialog.html',
                    controller: 'ExamStatusTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                creationDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exam-status-tcc', null, { reload: 'exam-status-tcc' });
                }, function() {
                    $state.go('exam-status-tcc');
                });
            }]
        })
        .state('exam-status-tcc.edit', {
            parent: 'exam-status-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-status/exam-status-tcc-dialog.html',
                    controller: 'ExamStatusTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExamStatus', function(ExamStatus) {
                            return ExamStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-status-tcc', null, { reload: 'exam-status-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-status-tcc.delete', {
            parent: 'exam-status-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-status/exam-status-tcc-delete-dialog.html',
                    controller: 'ExamStatusTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExamStatus', function(ExamStatus) {
                            return ExamStatus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-status-tcc', null, { reload: 'exam-status-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
