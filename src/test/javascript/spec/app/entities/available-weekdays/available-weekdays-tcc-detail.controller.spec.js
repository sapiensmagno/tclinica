'use strict';

describe('Controller Tests', function() {

    describe('AvailableWeekdays Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockAvailableWeekdays, MockDoctorSchedule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockAvailableWeekdays = jasmine.createSpy('MockAvailableWeekdays');
            MockDoctorSchedule = jasmine.createSpy('MockDoctorSchedule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'AvailableWeekdays': MockAvailableWeekdays,
                'DoctorSchedule': MockDoctorSchedule
            };
            createController = function() {
                $injector.get('$controller')("AvailableWeekdaysTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:availableWeekdaysUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
