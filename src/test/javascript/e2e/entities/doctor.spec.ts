import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Doctor e2e test', () => {

    let navBarPage: NavBarPage;
    let doctorDialogPage: DoctorDialogPage;
    let doctorComponentsPage: DoctorComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Doctors', () => {
        navBarPage.goToEntity('doctor');
        doctorComponentsPage = new DoctorComponentsPage();
        expect(doctorComponentsPage.getTitle()).toMatch(/tclinicaApp.doctor.home.title/);

    });

    it('should load create Doctor dialog', () => {
        doctorComponentsPage.clickOnCreateButton();
        doctorDialogPage = new DoctorDialogPage();
        expect(doctorDialogPage.getModalTitle()).toMatch(/tclinicaApp.doctor.home.createOrEditLabel/);
        doctorDialogPage.close();
    });

    it('should create and save Doctors', () => {
        doctorComponentsPage.clickOnCreateButton();
        doctorDialogPage.setSpecialtyInput('specialty');
        expect(doctorDialogPage.getSpecialtyInput()).toMatch('specialty');
        doctorDialogPage.userSelectLastOption();
        doctorDialogPage.save();
        expect(doctorDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class DoctorComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-doctor div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class DoctorDialogPage {
    modalTitle = element(by.css('h4#myDoctorLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    specialtyInput = element(by.css('input#field_specialty'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setSpecialtyInput = function (specialty) {
        this.specialtyInput.sendKeys(specialty);
    }

    getSpecialtyInput = function () {
        return this.specialtyInput.getAttribute('value');
    }

    userSelectLastOption = function () {
        this.userSelect.all(by.tagName('option')).last().click();
    }

    userSelectOption = function (option) {
        this.userSelect.sendKeys(option);
    }

    getUserSelect = function () {
        return this.userSelect;
    }

    getUserSelectedOption = function () {
        return this.userSelect.element(by.css('option:checked')).getText();
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
