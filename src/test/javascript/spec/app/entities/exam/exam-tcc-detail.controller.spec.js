'use strict';

describe('Controller Tests', function() {

    describe('Exam Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockExam, MockExamType, MockExamStatus, MockMedicalRecord;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockExam = jasmine.createSpy('MockExam');
            MockExamType = jasmine.createSpy('MockExamType');
            MockExamStatus = jasmine.createSpy('MockExamStatus');
            MockMedicalRecord = jasmine.createSpy('MockMedicalRecord');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Exam': MockExam,
                'ExamType': MockExamType,
                'ExamStatus': MockExamStatus,
                'MedicalRecord': MockMedicalRecord
            };
            createController = function() {
                $injector.get('$controller')("ExamTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:examUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
