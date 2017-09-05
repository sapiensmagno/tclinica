'use strict';

describe('Controller Tests', function() {

    describe('Prescription Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPrescription, MockMedicine, MockMedicalRecord;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPrescription = jasmine.createSpy('MockPrescription');
            MockMedicine = jasmine.createSpy('MockMedicine');
            MockMedicalRecord = jasmine.createSpy('MockMedicalRecord');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Prescription': MockPrescription,
                'Medicine': MockMedicine,
                'MedicalRecord': MockMedicalRecord
            };
            createController = function() {
                $injector.get('$controller')("PrescriptionTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:prescriptionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
