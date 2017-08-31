import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Patient } from './patient.model';
import { PatientPopupService } from './patient-popup.service';
import { PatientService } from './patient.service';

@Component({
    selector: 'jhi-patient-delete-dialog',
    templateUrl: './patient-delete-dialog.component.html'
})
export class PatientDeleteDialogComponent {

    patient: Patient;

    constructor(
        private patientService: PatientService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.patientService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'patientListModification',
                content: 'Deleted an patient'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-patient-delete-popup',
    template: ''
})
export class PatientDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private patientPopupService: PatientPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.patientPopupService
                .open(PatientDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
