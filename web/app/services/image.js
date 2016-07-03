export default class Image {
    static all(next) {
        request
            .get('/images')
            .use(Requests.auth)
            .end(Requests.response(next));
    }

    static search(query, next) {
        request
            .get(`/images/search/${encodeURIComponent(query)}`)
            .use(Requests.auth)
            .end(Requests.response(next));
    }

    static create(file, name, tags, next) {
        request
            .put('/images')
            .use(Requests.auth)
            .field('name', name)
            .field('tags', tags)
            .attach('image', file)
            .end(Requests.response(next));
    }

    static delete(imageId, next) {
        request
            .delete(`/images/${imageId}`)
            .use(Requests.auth)
            .end(Requests.response(next));
    }

    static get(imageId, next) {
        request
            .get(`/images/${encodeURIComponent(imageId)}`)
            .use(Requests.auth)
            .end(Requests.response(next));
    }
}
