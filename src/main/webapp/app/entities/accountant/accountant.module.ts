import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { TclinicaSharedModule } from '../../shared';
import { TclinicaAdminModule } from '../../admin/admin.module';
import {
    AccountantService,
    AccountantPopupService,
    AccountantComponent,
    AccountantDetailComponent,
    AccountantDialogComponent,
    AccountantPopupComponent,
    AccountantDeletePopupComponent,
    AccountantDeleteDialogComponent,
    accountantRoute,
    accountantPopupRoute,
} from './';

const ENTITY_STATES = [
    ...accountantRoute,
    ...accountantPopupRoute,
];

@NgModule({
    imports: [
        TclinicaSharedModule,
        TclinicaAdminModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AccountantComponent,
        AccountantDetailComponent,
        AccountantDialogComponent,
        AccountantDeleteDialogComponent,
        AccountantPopupComponent,
        AccountantDeletePopupComponent,
    ],
    entryComponents: [
        AccountantComponent,
        AccountantDialogComponent,
        AccountantPopupComponent,
        AccountantDeleteDialogComponent,
        AccountantDeletePopupComponent,
    ],
    providers: [
        AccountantService,
        AccountantPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class TclinicaAccountantModule {}
