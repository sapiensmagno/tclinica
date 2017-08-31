import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Accountant } from './accountant.model';
import { AccountantService } from './accountant.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-accountant',
    templateUrl: './accountant.component.html'
})
export class AccountantComponent implements OnInit, OnDestroy {
accountants: Accountant[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private accountantService: AccountantService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.accountantService.query().subscribe(
            (res: ResponseWrapper) => {
                this.accountants = res.json;
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInAccountants();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Accountant) {
        return item.id;
    }
    registerChangeInAccountants() {
        this.eventSubscriber = this.eventManager.subscribe('accountantListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
