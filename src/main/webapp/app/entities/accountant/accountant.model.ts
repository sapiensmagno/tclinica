import { BaseEntity, User } from './../../shared';

export class Accountant implements BaseEntity {
    constructor(
        public id?: number,
        public officeName?: string,
        public user?: User,
    ) {
    }
}
