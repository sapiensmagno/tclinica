import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Accountant } from './accountant.model';
import { AccountantPopupService } from './accountant-popup.service';
import { AccountantService } from './accountant.service';

@Component({
    selector: 'jhi-accountant-delete-dialog',
    templateUrl: './accountant-delete-dialog.component.html'
})
export class AccountantDeleteDialogComponent {

    accountant: Accountant;

    constructor(
        private accountantService: AccountantService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.accountantService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'accountantListModification',
                content: 'Deleted an accountant'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-accountant-delete-popup',
    template: ''
})
export class AccountantDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private accountantPopupService: AccountantPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.accountantPopupService
                .open(AccountantDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
