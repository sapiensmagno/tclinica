import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { JhiPaginationUtil } from 'ng-jhipster';

import { AccountantComponent } from './accountant.component';
import { AccountantDetailComponent } from './accountant-detail.component';
import { AccountantPopupComponent } from './accountant-dialog.component';
import { AccountantDeletePopupComponent } from './accountant-delete-dialog.component';

export const accountantRoute: Routes = [
    {
        path: 'accountant',
        component: AccountantComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tclinicaApp.accountant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'accountant/:id',
        component: AccountantDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tclinicaApp.accountant.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const accountantPopupRoute: Routes = [
    {
        path: 'accountant-new',
        component: AccountantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tclinicaApp.accountant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'accountant/:id/edit',
        component: AccountantPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tclinicaApp.accountant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'accountant/:id/delete',
        component: AccountantDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'tclinicaApp.accountant.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
