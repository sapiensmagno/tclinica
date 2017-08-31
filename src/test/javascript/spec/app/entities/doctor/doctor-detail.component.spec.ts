/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TclinicaTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DoctorDetailComponent } from '../../../../../../main/webapp/app/entities/doctor/doctor-detail.component';
import { DoctorService } from '../../../../../../main/webapp/app/entities/doctor/doctor.service';
import { Doctor } from '../../../../../../main/webapp/app/entities/doctor/doctor.model';

describe('Component Tests', () => {

    describe('Doctor Management Detail Component', () => {
        let comp: DoctorDetailComponent;
        let fixture: ComponentFixture<DoctorDetailComponent>;
        let service: DoctorService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TclinicaTestModule],
                declarations: [DoctorDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DoctorService,
                    JhiEventManager
                ]
            }).overrideTemplate(DoctorDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DoctorDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DoctorService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Doctor(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.doctor).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
