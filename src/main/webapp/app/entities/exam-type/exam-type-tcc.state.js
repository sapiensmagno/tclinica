(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('exam-type-tcc', {
            parent: 'entity',
            url: '/exam-type-tcc?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.examType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam-type/exam-typesTCC.html',
                    controller: 'ExamTypeTccController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('exam-type-tcc-detail', {
            parent: 'exam-type-tcc',
            url: '/exam-type-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.examType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/exam-type/exam-type-tcc-detail.html',
                    controller: 'ExamTypeTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('examType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ExamType', function($stateParams, ExamType) {
                    return ExamType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'exam-type-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('exam-type-tcc-detail.edit', {
            parent: 'exam-type-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-type/exam-type-tcc-dialog.html',
                    controller: 'ExamTypeTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-type-tcc.new', {
            parent: 'exam-type-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-type/exam-type-tcc-dialog.html',
                    controller: 'ExamTypeTccDialogController',
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
                    $state.go('exam-type-tcc', null, { reload: 'exam-type-tcc' });
                }, function() {
                    $state.go('exam-type-tcc');
                });
            }]
        })
        .state('exam-type-tcc.edit', {
            parent: 'exam-type-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-type/exam-type-tcc-dialog.html',
                    controller: 'ExamTypeTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-type-tcc', null, { reload: 'exam-type-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('exam-type-tcc.delete', {
            parent: 'exam-type-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/exam-type/exam-type-tcc-delete-dialog.html',
                    controller: 'ExamTypeTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ExamType', function(ExamType) {
                            return ExamType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('exam-type-tcc', null, { reload: 'exam-type-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
