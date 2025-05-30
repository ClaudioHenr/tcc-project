export interface list {
    id: number;
    name: string;
    description: string;
    gradeIds: number[];
    exerciseIds: number[];
    dueDate?: Date;
 }