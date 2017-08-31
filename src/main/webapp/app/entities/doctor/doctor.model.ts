import { BaseEntity, User } from './../../shared';

export class Doctor implements BaseEntity {
    constructor(
        public id?: number,
        public specialty?: string,
        public user?: User,
    ) {
    }
}
