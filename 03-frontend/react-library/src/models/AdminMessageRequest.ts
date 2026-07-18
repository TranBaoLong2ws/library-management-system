class AdminMessageRequest {

    id: number;
    reponse: string;


    constructor(id: number, reponse: string) {
        this.id = id;
        this.reponse = reponse;

    }
}

export default AdminMessageRequest;