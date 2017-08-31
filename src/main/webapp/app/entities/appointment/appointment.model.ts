import { BaseEntity } from './../../shared';

export class Appointment implements BaseEntity {
    constructor(
        public id?: number,
        public scheduledDate?: any,
        public cancelled?: boolean,
        public patient?: BaseEntity,
        public doctor?: BaseEntity,
    ) {
        this.cancelled = false;
    }
}
