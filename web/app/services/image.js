export default class Image {
    static all(next) {
        request
            .get('/images')
            .use(Requests.auth)
            .end(Requests.response(next));
    }

    static create(file, name, next) {
        request
            .put('/images')
            .use(Requests.auth)
            .field('name', name)
            .attach('image', file)
            .end(Requests.response(next));
    }

    static delete(imageId, next) {
        request
            .delete(`/images/${imageId}`)
            .use(Requests.auth)
            .end(Requests.response(next));
    }
}