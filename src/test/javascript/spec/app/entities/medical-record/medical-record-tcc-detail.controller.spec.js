'use strict';

describe('Controller Tests', function() {

    describe('MedicalRecord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockMedicalRecord, MockAppointment, MockExam, MockPrescription;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockMedicalRecord = jasmine.createSpy('MockMedicalRecord');
            MockAppointment = jasmine.createSpy('MockAppointment');
            MockExam = jasmine.createSpy('MockExam');
            MockPrescription = jasmine.createSpy('MockPrescription');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'MedicalRecord': MockMedicalRecord,
                'Appointment': MockAppointment,
                'Exam': MockExam,
                'Prescription': MockPrescription
            };
            createController = function() {
                $injector.get('$controller')("MedicalRecordTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:medicalRecordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
