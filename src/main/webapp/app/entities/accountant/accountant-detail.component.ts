import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Accountant } from './accountant.model';
import { AccountantService } from './accountant.service';

@Component({
    selector: 'jhi-accountant-detail',
    templateUrl: './accountant-detail.component.html'
})
export class AccountantDetailComponent implements OnInit, OnDestroy {

    accountant: Accountant;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private accountantService: AccountantService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAccountants();
    }

    load(id) {
        this.accountantService.find(id).subscribe((accountant) => {
            this.accountant = accountant;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAccountants() {
        this.eventSubscriber = this.eventManager.subscribe(
            'accountantListModification',
            (response) => this.load(this.accountant.id)
        );
    }
}
