'use strict';

describe('Controller Tests', function() {

    describe('DoctorSchedule Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDoctorSchedule, MockDoctor, MockAvailableWeekdays;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDoctorSchedule = jasmine.createSpy('MockDoctorSchedule');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockAvailableWeekdays = jasmine.createSpy('MockAvailableWeekdays');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DoctorSchedule': MockDoctorSchedule,
                'Doctor': MockDoctor,
                'AvailableWeekdays': MockAvailableWeekdays
            };
            createController = function() {
                $injector.get('$controller')("DoctorScheduleTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:doctorScheduleUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
