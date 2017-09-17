(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exam-tcc', {
            parent: 'entity',
            url: '/exam-tcc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.exam.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam/examsTCC.html',
                    controller: 'ExamTccController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('exam');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exam-tcc-detail', {
            parent: 'exam-tcc',
            url: '/exam-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.exam.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam/exam-tcc-detail.html',
                    controller: 'ExamTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('exam');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Exam', function($stateParams, Exam) {
                    return Exam.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exam-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exam-tcc-detail.edit', {
            parent: 'exam-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam/exam-tcc-dialog.html',
                    controller: 'ExamTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-tcc.new', {
            parent: 'exam-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam/exam-tcc-dialog.html',
                    controller: 'ExamTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('exam-tcc', null, { reload: 'exam-tcc' });
                }, function() {
                    $state.go('exam-tcc');
                });
            }]
        })
        .state('exam-tcc.edit', {
            parent: 'exam-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam/exam-tcc-dialog.html',
                    controller: 'ExamTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-tcc', null, { reload: 'exam-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-tcc.delete', {
            parent: 'exam-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam/exam-tcc-delete-dialog.html',
                    controller: 'ExamTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Exam', function(Exam) {
                            return Exam.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-tcc', null, { reload: 'exam-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
