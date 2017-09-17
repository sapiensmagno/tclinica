'use strict';

describe('Controller Tests', function() {

    describe('PaymentInstallment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPaymentInstallment, MockPaymentMethod, MockCardBrand, MockHealthcare, MockAppointment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPaymentInstallment = jasmine.createSpy('MockPaymentInstallment');
            MockPaymentMethod = jasmine.createSpy('MockPaymentMethod');
            MockCardBrand = jasmine.createSpy('MockCardBrand');
            MockHealthcare = jasmine.createSpy('MockHealthcare');
            MockAppointment = jasmine.createSpy('MockAppointment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PaymentInstallment': MockPaymentInstallment,
                'PaymentMethod': MockPaymentMethod,
                'CardBrand': MockCardBrand,
                'Healthcare': MockHealthcare,
                'Appointment': MockAppointment
            };
            createController = function() {
                $injector.get('$controller')("PaymentInstallmentTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:paymentInstallmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
