'use strict';

describe('Controller Tests', function() {

    describe('Appointment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAppointment, MockPaymentInstallment, MockPatient, MockDoctor, MockDoctorSchedule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAppointment = jasmine.createSpy('MockAppointment');
            MockPaymentInstallment = jasmine.createSpy('MockPaymentInstallment');
            MockPatient = jasmine.createSpy('MockPatient');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockDoctorSchedule = jasmine.createSpy('MockDoctorSchedule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Appointment': MockAppointment,
                'PaymentInstallment': MockPaymentInstallment,
                'Patient': MockPatient,
                'Doctor': MockDoctor,
                'DoctorSchedule': MockDoctorSchedule
            };
            createController = function() {
                $injector.get('$controller')("AppointmentTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:appointmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
