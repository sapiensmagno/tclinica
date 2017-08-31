import { BaseEntity, User } from './../../shared';

export class Patient implements BaseEntity {
    constructor(
        public id?: number,
        public gender?: string,
        public user?: User,
    ) {
    }
}
