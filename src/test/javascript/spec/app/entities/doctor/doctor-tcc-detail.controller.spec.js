'use strict';

describe('Controller Tests', function() {

    describe('Doctor Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDoctor, MockUser, MockDoctorSchedule;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDoctor = jasmine.createSpy('MockDoctor');
            MockUser = jasmine.createSpy('MockUser');
            MockDoctorSchedule = jasmine.createSpy('MockDoctorSchedule');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Doctor': MockDoctor,
                'User': MockUser,
                'DoctorSchedule': MockDoctorSchedule
            };
            createController = function() {
                $injector.get('$controller')("DoctorTccDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tclinicaApp:doctorUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
