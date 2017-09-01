(function() {
    'use strict';

    angular
        .module('tclinicaApp')
        .controller('DoctorScheduleTccDialogController', DoctorScheduleTccDialogController);

    DoctorScheduleTccDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'DoctorSchedule', 'Doctor', 'AvailableWeekdays'];

    function DoctorScheduleTccDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, DoctorSchedule, Doctor, AvailableWeekdays) {
        var vm = this;

        vm.doctorSchedule = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.doctors = Doctor.query({filter: 'doctorschedule-is-null'});
        $q.all([vm.doctorSchedule.$promise, vm.doctors.$promise]).then(function() {
            if (!vm.doctorSchedule.doctor || !vm.doctorSchedule.doctor.id) {
                return $q.reject();
            }
            return Doctor.get({id : vm.doctorSchedule.doctor.id}).$promise;
        }).then(function(doctor) {
            vm.doctors.push(doctor);
        });
        vm.availableweekdays = AvailableWeekdays.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.doctorSchedule.id !== null) {
                DoctorSchedule.update(vm.doctorSchedule, onSaveSuccess, onSaveError);
            } else {
                DoctorSchedule.save(vm.doctorSchedule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('tclinicaApp:doctorScheduleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.earliestAppointmentTime = false;
        vm.datePickerOpenStatus.latestAppointmentTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
