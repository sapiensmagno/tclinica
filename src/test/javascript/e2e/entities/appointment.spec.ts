import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Appointment e2e test', () => {

    let navBarPage: NavBarPage;
    let appointmentDialogPage: AppointmentDialogPage;
    let appointmentComponentsPage: AppointmentComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Appointments', () => {
        navBarPage.goToEntity('appointment');
        appointmentComponentsPage = new AppointmentComponentsPage();
        expect(appointmentComponentsPage.getTitle()).toMatch(/tclinicaApp.appointment.home.title/);

    });

    it('should load create Appointment dialog', () => {
        appointmentComponentsPage.clickOnCreateButton();
        appointmentDialogPage = new AppointmentDialogPage();
        expect(appointmentDialogPage.getModalTitle()).toMatch(/tclinicaApp.appointment.home.createOrEditLabel/);
        appointmentDialogPage.close();
    });

    it('should create and save Appointments', () => {
        appointmentComponentsPage.clickOnCreateButton();
        appointmentDialogPage.setScheduledDateInput(12310020012301);
        expect(appointmentDialogPage.getScheduledDateInput()).toMatch('2001-12-31T02:30');
        appointmentDialogPage.getCancelledInput().isSelected().then(function (selected) {
            if (selected) {
                appointmentDialogPage.getCancelledInput().click();
                expect(appointmentDialogPage.getCancelledInput().isSelected()).toBeFalsy();
            } else {
                appointmentDialogPage.getCancelledInput().click();
                expect(appointmentDialogPage.getCancelledInput().isSelected()).toBeTruthy();
            }
        });
        appointmentDialogPage.patientSelectLastOption();
        appointmentDialogPage.doctorSelectLastOption();
        appointmentDialogPage.save();
        expect(appointmentDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AppointmentComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-appointment div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AppointmentDialogPage {
    modalTitle = element(by.css('h4#myAppointmentLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    scheduledDateInput = element(by.css('input#field_scheduledDate'));
    cancelledInput = element(by.css('input#field_cancelled'));
    patientSelect = element(by.css('select#field_patient'));
    doctorSelect = element(by.css('select#field_doctor'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setScheduledDateInput = function (scheduledDate) {
        this.scheduledDateInput.sendKeys(scheduledDate);
    }

    getScheduledDateInput = function () {
        return this.scheduledDateInput.getAttribute('value');
    }

    getCancelledInput = function () {
        return this.cancelledInput;
    }
    patientSelectLastOption = function () {
        this.patientSelect.all(by.tagName('option')).last().click();
    }

    patientSelectOption = function (option) {
        this.patientSelect.sendKeys(option);
    }

    getPatientSelect = function () {
        return this.patientSelect;
    }

    getPatientSelectedOption = function () {
        return this.patientSelect.element(by.css('option:checked')).getText();
    }

    doctorSelectLastOption = function () {
        this.doctorSelect.all(by.tagName('option')).last().click();
    }

    doctorSelectOption = function (option) {
        this.doctorSelect.sendKeys(option);
    }

    getDoctorSelect = function () {
        return this.doctorSelect;
    }

    getDoctorSelectedOption = function () {
        return this.doctorSelect.element(by.css('option:checked')).getText();
    }

    save() {
        this.saveButton.click();
    }

    close() {
        this.closeButton.click();
    }

    getSaveButton() {
        return this.saveButton;
    }
}
