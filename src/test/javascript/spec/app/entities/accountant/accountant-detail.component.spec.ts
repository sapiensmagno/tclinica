/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TclinicaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { AccountantDetailComponent } from '../../../../../../main/webapp/app/entities/accountant/accountant-detail.component';
import { AccountantService } from '../../../../../../main/webapp/app/entities/accountant/accountant.service';
import { Accountant } from '../../../../../../main/webapp/app/entities/accountant/accountant.model';

describe('Component Tests', () => {

    describe('Accountant Management Detail Component', () => {
        let comp: AccountantDetailComponent;
        let fixture: ComponentFixture<AccountantDetailComponent>;
        let service: AccountantService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TclinicaTestModule],
                declarations: [AccountantDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    AccountantService,
                    JhiEventManager
                ]
            }).overrideTemplate(AccountantDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(AccountantDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AccountantService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Accountant(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.accountant).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
