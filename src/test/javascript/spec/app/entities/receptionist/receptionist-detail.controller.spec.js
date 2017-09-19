'use strict';

describe('Controller Tests', function() {

    describe('Receptionist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockReceptionist, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockReceptionist = jasmine.createSpy('MockReceptionist');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Receptionist': MockReceptionist,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("ReceptionistDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:receptionistUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
