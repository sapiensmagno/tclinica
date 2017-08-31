import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { TclinicaDoctorModule } from './doctor/doctor.module';
import { TclinicaPatientModule } from './patient/patient.module';
import { TclinicaAccountantModule } from './accountant/accountant.module';
import { TclinicaAppointmentModule } from './appointment/appointment.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        TclinicaDoctorModule,
        TclinicaPatientModule,
        TclinicaAccountantModule,
        TclinicaAppointmentModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TclinicaEntityModule {}
