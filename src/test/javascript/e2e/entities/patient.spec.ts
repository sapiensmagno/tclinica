import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Patient e2e test', () => {

    let navBarPage: NavBarPage;
    let patientDialogPage: PatientDialogPage;
    let patientComponentsPage: PatientComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Patients', () => {
        navBarPage.goToEntity('patient');
        patientComponentsPage = new PatientComponentsPage();
        expect(patientComponentsPage.getTitle()).toMatch(/tclinicaApp.patient.home.title/);

    });

    it('should load create Patient dialog', () => {
        patientComponentsPage.clickOnCreateButton();
        patientDialogPage = new PatientDialogPage();
        expect(patientDialogPage.getModalTitle()).toMatch(/tclinicaApp.patient.home.createOrEditLabel/);
        patientDialogPage.close();
    });

    it('should create and save Patients', () => {
        patientComponentsPage.clickOnCreateButton();
        patientDialogPage.setGenderInput('gender');
        expect(patientDialogPage.getGenderInput()).toMatch('gender');
        patientDialogPage.userSelectLastOption();
        patientDialogPage.save();
        expect(patientDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class PatientComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-patient div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class PatientDialogPage {
    modalTitle = element(by.css('h4#myPatientLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    genderInput = element(by.css('input#field_gender'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setGenderInput = function (gender) {
        this.genderInput.sendKeys(gender);
    }

    getGenderInput = function () {
        return this.genderInput.getAttribute('value');
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
