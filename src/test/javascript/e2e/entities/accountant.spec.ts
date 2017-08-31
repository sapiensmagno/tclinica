import { browser, element, by, $ } from 'protractor';
import { NavBarPage } from './../page-objects/jhi-page-objects';
const path = require('path');

describe('Accountant e2e test', () => {

    let navBarPage: NavBarPage;
    let accountantDialogPage: AccountantDialogPage;
    let accountantComponentsPage: AccountantComponentsPage;
    const fileToUpload = '../../../../main/webapp/content/images/logo-jhipster.png';
    const absolutePath = path.resolve(__dirname, fileToUpload);
    

    beforeAll(() => {
        browser.get('/');
        browser.waitForAngular();
        navBarPage = new NavBarPage();
        navBarPage.getSignInPage().autoSignInUsing('admin', 'admin');
        browser.waitForAngular();
    });

    it('should load Accountants', () => {
        navBarPage.goToEntity('accountant');
        accountantComponentsPage = new AccountantComponentsPage();
        expect(accountantComponentsPage.getTitle()).toMatch(/tclinicaApp.accountant.home.title/);

    });

    it('should load create Accountant dialog', () => {
        accountantComponentsPage.clickOnCreateButton();
        accountantDialogPage = new AccountantDialogPage();
        expect(accountantDialogPage.getModalTitle()).toMatch(/tclinicaApp.accountant.home.createOrEditLabel/);
        accountantDialogPage.close();
    });

    it('should create and save Accountants', () => {
        accountantComponentsPage.clickOnCreateButton();
        accountantDialogPage.setOfficeNameInput('officeName');
        expect(accountantDialogPage.getOfficeNameInput()).toMatch('officeName');
        accountantDialogPage.userSelectLastOption();
        accountantDialogPage.save();
        expect(accountantDialogPage.getSaveButton().isPresent()).toBeFalsy();
    }); 

    afterAll(() => {
        navBarPage.autoSignOut();
    });
});

export class AccountantComponentsPage {
    createButton = element(by.css('.jh-create-entity'));
    title = element.all(by.css('jhi-accountant div h2 span')).first();

    clickOnCreateButton() {
        return this.createButton.click();
    }

    getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class AccountantDialogPage {
    modalTitle = element(by.css('h4#myAccountantLabel'));
    saveButton = element(by.css('.modal-footer .btn.btn-primary'));
    closeButton = element(by.css('button.close'));
    officeNameInput = element(by.css('input#field_officeName'));
    userSelect = element(by.css('select#field_user'));

    getModalTitle() {
        return this.modalTitle.getAttribute('jhiTranslate');
    }

    setOfficeNameInput = function (officeName) {
        this.officeNameInput.sendKeys(officeName);
    }

    getOfficeNameInput = function () {
        return this.officeNameInput.getAttribute('value');
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
