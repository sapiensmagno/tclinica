(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('medicine-tcc', {
            parent: 'entity',
            url: '/medicine-tcc?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.medicine.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicine/medicinesTCC.html',
                    controller: 'MedicineTccController',
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
                    $translatePartialLoader.addPart('medicine');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('medicine-tcc-detail', {
            parent: 'medicine-tcc',
            url: '/medicine-tcc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'tclinicaApp.medicine.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/medicine/medicine-tcc-detail.html',
                    controller: 'MedicineTccDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('medicine');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Medicine', function($stateParams, Medicine) {
                    return Medicine.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'medicine-tcc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('medicine-tcc-detail.edit', {
            parent: 'medicine-tcc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-tcc-dialog.html',
                    controller: 'MedicineTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicine-tcc.new', {
            parent: 'medicine-tcc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-tcc-dialog.html',
                    controller: 'MedicineTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                genericName: null,
                                brandName: null,
                                manufacturer: null,
                                inactive: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('medicine-tcc', null, { reload: 'medicine-tcc' });
                }, function() {
                    $state.go('medicine-tcc');
                });
            }]
        })
        .state('medicine-tcc.edit', {
            parent: 'medicine-tcc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-tcc-dialog.html',
                    controller: 'MedicineTccDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicine-tcc', null, { reload: 'medicine-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('medicine-tcc.delete', {
            parent: 'medicine-tcc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/medicine/medicine-tcc-delete-dialog.html',
                    controller: 'MedicineTccDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Medicine', function(Medicine) {
                            return Medicine.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('medicine-tcc', null, { reload: 'medicine-tcc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
