'use strict';

describe('Controller Tests', function() {

    describe('Healthcare Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockHealthcare, MockPaymentInstallment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockHealthcare = jasmine.createSpy('MockHealthcare');
            MockPaymentInstallment = jasmine.createSpy('MockPaymentInstallment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Healthcare': MockHealthcare,
                'PaymentInstallment': MockPaymentInstallment
            };
            createController = function() {
                $injector.get('$controller')("HealthcareTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:healthcareUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
